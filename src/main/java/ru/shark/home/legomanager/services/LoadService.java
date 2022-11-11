package ru.shark.home.legomanager.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.dto.load.MissingSetPartsResponseDto;
import ru.shark.home.legomanager.datamanager.PartColorDataManager;
import ru.shark.home.legomanager.exception.RemoteDataException;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class LoadService extends BaseLogicService {
    protected static final String SOURCE_PORTAL = "https://www.bricklink.com/v2/catalog/";
    protected static final String SET_ID_URL = "catalogitem.page?S=%s-1";
    protected static final String PARTS_URL = "catalogitem_invtab.page?idItem=%s&st=1&show_invid=0&show_matchcolor=1&show_pglink=0&show_pcc=1&show_missingpcc=1&itemNoSeq=%s-1";

    private PartColorDataManager partColorDataManager;
    private RemoteDataProvider remoteDataProvider;

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

    private String getSetIdFromResponse(String response) {
        return Stream.of(response.split("\n"))
                .filter(item -> item.contains("idItem:"))
                .findFirst()
                .map(item -> item.split("\t")[item.split("\t").length - 1].trim())
                .orElse(null);
    }

    private List<MissingSetPartsResponseDto> getMissingParts(String setId, String setNumber) {
        List<PartColorDto> list = partColorDataManager.findALl();
        return getPartsFromRemote(setId, setNumber)
                .stream()
                .filter(item -> !item.getName().contains("Sticker Sheet") && !containsPartColor(list, item))
                .collect(Collectors.toList());
    }

    private boolean containsPartColor(List<PartColorDto> list, MissingSetPartsResponseDto responseDto) {
        for (PartColorDto partColorDto : list) {
            List<String> numbers = Stream.of(partColorDto.getPart().getNumber()).collect(Collectors.toList());
            if (!StringUtils.isBlank(partColorDto.getPart().getAlternateNumber())) {
                numbers.addAll(Arrays.stream(partColorDto.getPart().getAlternateNumber().split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()));
            }
            List<String> colorNums = Stream.of(partColorDto.getNumber()).collect(Collectors.toList());
            if (!StringUtils.isBlank(partColorDto.getAlternateNumber())) {
                colorNums.addAll(Arrays.stream(partColorDto.getAlternateNumber().split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()));
            }
            List<String> responseColors = Arrays.stream(responseDto.getColorNumber().split(",")).map(String::trim)
                    .collect(Collectors.toList());
            if (numbers.contains(responseDto.getNumber()) && responseColors.stream().anyMatch(colorNums::contains)) {
                return true;
            }
        }
        return false;
    }

    private List<MissingSetPartsResponseDto> getPartsFromRemote(String setId, String setNumber) {
        String partsData = remoteDataProvider.getDataFromUrl(String.format(SOURCE_PORTAL + PARTS_URL, setId, setNumber),
                "Не удалось получить данные по деталям набора с id " + setId);
        String[] rows = partsData.split("\n");
        int idx = 0;
        boolean tableStarted = false;
        List<MissingSetPartsResponseDto> result = new ArrayList<>();
        MissingSetPartsResponseDto dto;
        while (idx < rows.length) {
            String row = rows[idx];
            if (row.contains("pciinvItemTypeHeader")) {
                tableStarted = true;
            } else if (tableStarted && row.contains("TD") && row.contains("src=")) {
                dto = new MissingSetPartsResponseDto();
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
                dto.setName(lastRow.substring(3, lastRow.indexOf("</b>")));
                lastRow = lastRow.substring(lastRow.indexOf("pciinvPartsColorCode"));
                dto.setColorNumber(lastRow.substring(22, lastRow.indexOf("</SPAN>"))
                        .replace("<span style='color: black;'>", "")
                        .replace("</span>", "")
                        .replace("or", ",")
                );
                result.add(dto);
                idx = idx + 3;
            } else if (tableStarted && row.contains("Extra Items")) {
                break;
            }
            idx++;
        }
        return result;
    }

    @Autowired
    public void setPartColorDataManager(PartColorDataManager partColorDataManager) {
        this.partColorDataManager = partColorDataManager;
    }

    @Autowired
    public void setRemoteDataProvider(RemoteDataProvider remoteDataProvider) {
        this.remoteDataProvider = remoteDataProvider;
    }
}
