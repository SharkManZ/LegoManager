package ru.shark.home.legomanager.services;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.load.RemoteSetPartsDto;
import ru.shark.home.legomanager.datamanager.PartColorDataManager;
import ru.shark.home.legomanager.exception.RemoteDataException;
import ru.shark.home.legomanager.loader.SetDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class LoadService extends BaseLogicService {
    protected static final String SOURCE_PORTAL = "https://www.bricklink.com/v2/catalog/";
    protected static final String SET_ID_URL = "catalogitem.page?S=%s-1";
    protected static final String PARTS_URL = "catalogitem_invtab.page?idItem=%s&st=1&show_invid=0&show_matchcolor=1&show_pglink=0&show_pcc=1&show_missingpcc=1&itemNoSeq=%s-1";

    private PartColorDataManager partColorDataManager;
    private RemoteDataProvider remoteDataProvider;
    private SetDataLoader setDataLoader;
    private final Map<String, Pair<String, String>> partsComparison;

    public LoadService() {
        partsComparison = Maps.newHashMap();
        partsComparison.put("6628_Black Technic, Pin with Friction Ridges and Tow Ball (Undetermined Type)",
                Pair.of("6628a", "4184169"));
        partsComparison.put("6628_Red Technic, Pin with Friction Ridges and Tow Ball (Undetermined Type)",
                Pair.of("6628a", "6254216"));
        partsComparison.put("66645_Plastic Sails with '42105', 'Kool Keels', 'Anchor Bouy', and Waves with Dots Pattern, Sheet of 2",
                Pair.of("66645", "6289128"));
        partsComparison.put("5102c10_Black Hose, Pneumatic 4mm D. 10L / 8.0cm",
                Pair.of("5102c10", "6217913"));
    }

    public BaseResponse checkParts(String setNumber) {
        BaseResponse response;
        try {
            String setData = remoteDataProvider.getDataFromUrl(String.format(SOURCE_PORTAL + SET_ID_URL, setNumber),
                    "Не удалось получить идентификатор набора " + setNumber);
            String setId = getSetIdFromResponse(setData);
            response = new BaseResponse();
            response.setBody(getMissingParts(setId, setNumber));
            response.setSuccess(true);
        } catch (RemoteDataException e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении данных с удаленного источника " +
                    ": " + e.getMessage());
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении данных по набору " +
                    setNumber + ": " + e.getMessage());
        }

        return response;
    }

    public BaseResponse loadSetParts(String setNumber) {
        BaseResponse response;
        try {
            String setData = remoteDataProvider.getDataFromUrl(String.format(SOURCE_PORTAL + SET_ID_URL, setNumber),
                    "Не удалось получить идентификатор набора " + setNumber);
            String setId = getSetIdFromResponse(setData);
            setDataLoader.loadSetParts(setNumber, getPartsFromRemote(setId, setNumber));
            response = new BaseResponse();
            response.setSuccess(true);
        } catch (RemoteDataException e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении данных с удаленного источника " +
                    ": " + e.getMessage());
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при загрузка деталей набора " + setNumber +
                    ": " + e.getMessage());
        }

        return response;
    }

    private String getSetIdFromResponse(String response) {
        return Stream.of(response.split("\n"))
                .filter(item -> item.contains("idItem:"))
                .findFirst()
                .map(item -> item.split("\t")[item.split("\t").length - 1].trim())
                .orElse(null);
    }

    private List<RemoteSetPartsDto> getMissingParts(String setId, String setNumber) {
        return setDataLoader.findMissingParts(getPartsFromRemote(setId, setNumber));
    }

    private List<RemoteSetPartsDto> getPartsFromRemote(String setId, String setNumber) {
        String partsData = remoteDataProvider.getDataFromUrl(String.format(SOURCE_PORTAL + PARTS_URL, setId, setNumber),
                "Не удалось получить данные по деталям набора с id " + setId);
        String[] rows = partsData.split("\n");
        int idx = 0;
        long id = 0L;
        boolean tableStarted = false;
        List<RemoteSetPartsDto> result = new ArrayList<>();
        RemoteSetPartsDto dto;
        while (idx < rows.length) {
            String row = rows[idx];
            if (row.contains("pciinvItemTypeHeader")) {
                tableStarted = true;
            } else if (tableStarted && row.contains("TD") && row.contains("src=")) {
                id++;
                dto = new RemoteSetPartsDto();
                dto.setId(id);
                String rowPart = row.substring(row.indexOf("src=")).replace("src=\"", "");
                dto.setImgUrl(rowPart.substring(2, rowPart.indexOf("\" ")));
                String countRow = rows[idx + 1];
                dto.setCount(Integer.valueOf(countRow.replace("<TD>", "").replace("</TD>", "")
                        .replace("\t", "").trim()));
                String numberRow = rows[idx + 2];
                numberRow = numberRow.substring(numberRow.indexOf("\">"));
                dto.setNumber(numberRow.substring(2, numberRow.indexOf("</A>")));
                String lastRow = rows[idx + 3];
                lastRow = lastRow.substring(lastRow.indexOf("<b>"));
                String partName = lastRow.substring(3, lastRow.indexOf("</b>"));
                if (partName.toLowerCase().contains("sticker sheet") || partName.toLowerCase().contains("leaflet")) {
                    idx++;
                    continue;
                }
                dto.setName(partName);
                lastRow = lastRow.substring(lastRow.indexOf("pciinvPartsColorCode"));
                dto.setColorNumber(lastRow.substring(22, lastRow.indexOf("</SPAN>"))
                        .replace("<span style='color: black;'>", "")
                        .replace("</span>", "")
                        .replace(" ", "")
                        .replace("or", ", ")
                );
                result.add(checkPartComparison(dto));
                idx = idx + 3;
            } else if (tableStarted && row.contains("Extra Items")) {
                break;
            }
            idx++;
        }
        return result;
    }

    private RemoteSetPartsDto checkPartComparison(RemoteSetPartsDto dto) {
        String partKey = dto.getNumber().trim() + "_" + dto.getName().trim();
        Pair<String, String> comparison = partsComparison.getOrDefault(partKey, null);
        if (comparison != null) {
            dto.setNumber(comparison.getLeft());
            dto.setColorNumber(comparison.getRight());
        }
        return dto;
    }

    @Autowired
    public void setPartColorDataManager(PartColorDataManager partColorDataManager) {
        this.partColorDataManager = partColorDataManager;
    }

    @Autowired
    public void setRemoteDataProvider(RemoteDataProvider remoteDataProvider) {
        this.remoteDataProvider = remoteDataProvider;
    }

    @Autowired
    public void setSetDataLoader(SetDataLoader setDataLoader) {
        this.setDataLoader = setDataLoader;
    }
}
