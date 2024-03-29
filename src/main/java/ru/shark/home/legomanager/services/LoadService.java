package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.dao.dto.load.RemoteSetPartDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.repository.PartLoadComparisonRepository;
import ru.shark.home.legomanager.datamanager.PartColorDataManager;
import ru.shark.home.legomanager.datamanager.PartLoadSkipDataManager;
import ru.shark.home.legomanager.exception.RemoteDataException;
import ru.shark.home.legomanager.loader.SetDataLoader;
import ru.shark.home.legomanager.services.dto.RemoteSetPartsDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class LoadService extends BaseLogicService {
    protected static final String SOURCE_PORTAL = "https://www.bricklink.com/v2/catalog/";
    protected static final String SET_ID_URL = "catalogitem.page?S=%s-1";
    protected static final String PARTS_URL = "catalogitem_invtab.page?idItem=%s&st=1&show_invid=0&show_matchcolor=1&" +
            "show_pglink=0&show_pcc=1&show_missingpcc=1&itemNoSeq=%s-1";

    private PartColorDataManager partColorDataManager;
    private RemoteDataProvider remoteDataProvider;
    private SetDataLoader setDataLoader;
    private PartLoadSkipDataManager partLoadSkipDataManager;
    private PartLoadComparisonRepository partLoadComparisonRepository;

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

    public BaseResponse getPartLoadSkipList(PageRequest request) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(partLoadSkipDataManager.getWithPagination(getCriteria(request, PartLoadSkipDto.class)));
            response.setSuccess(true);
        } catch (Exception ex) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка деталей пропускаемых при импорте: " + ex.getMessage());
        }

        return response;
    }

    public BaseResponse partLoadSkipSave(PartLoadSkipDto dto) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(partLoadSkipDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception ex) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении пропускаемой при импорте детали: " + ex.getMessage());
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

    private RemoteSetPartsDto getMissingParts(String setId, String setNumber) {
        List<RemoteSetPartDto> parts = getPartsFromRemote(setId, setNumber);
        RemoteSetPartsDto result = new RemoteSetPartsDto();
        result.setDiffPartsCount(parts.size());
        result.setParts(setDataLoader.findMissingParts(parts));
        result.setMissingDiffPartsCount(result.getParts().size());
        return result;
    }

    private List<RemoteSetPartDto> getPartsFromRemote(String setId, String setNumber) {
        List<String> skipPatterns = partLoadSkipDataManager.findALl().stream().map(PartLoadSkipDto::getPattern)
                .collect(Collectors.toList());
        String partsData = remoteDataProvider.getDataFromUrl(String.format(SOURCE_PORTAL + PARTS_URL, setId, setNumber),
                "Не удалось получить данные по деталям набора с id " + setId);
        String[] rows = partsData.split("\n");
        int idx = 0;
        long id = 0L;
        boolean tableStarted = false;
        List<RemoteSetPartDto> parts = new ArrayList<>();
        RemoteSetPartDto dto;
        while (idx < rows.length) {
            String row = rows[idx];
            if (row.contains("pciinvItemTypeHeader")) {
                tableStarted = true;
            } else if (tableStarted && row.contains("TD") && row.contains("src=")) {
                id++;
                dto = new RemoteSetPartDto();
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
                String partName = lastRow.substring(3, lastRow.indexOf("</b>")).replaceAll("\\s+", " ").trim();

                if (skipPatterns.stream().anyMatch(item -> partName.toLowerCase().contains(item.toLowerCase()))) {
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
                if (dto.getColorNumber().contains("CodeMissing")) {
                    dto.setColorNumber("");
                }
                parts.add(checkPartComparison(dto));
                idx = idx + 3;
            } else if (tableStarted && row.contains("Extra Items")) {
                break;
            }
            idx++;
        }
        return parts;
    }

    private RemoteSetPartDto checkPartComparison(RemoteSetPartDto dto) {
        if (!isEmpty(dto.getColorNumber())) {
            return dto;
        }
        String number = dto.getNumber().trim();
        String name = dto.getName().trim().replaceAll("\\s+", " ");
        PartColorEntity comparisonPartColor = partLoadComparisonRepository.findPartColorByLoadPartComparisonByNumberAndName(number, name);
        if (comparisonPartColor != null) {
            dto.setComparisonPartColorId(comparisonPartColor.getId());
            return dto;
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

    @Autowired
    public void setPartLoadSkipDataManager(PartLoadSkipDataManager partLoadSkipDataManager) {
        this.partLoadSkipDataManager = partLoadSkipDataManager;
    }

    @Autowired
    public void setPartLoadComparisonRepository(PartLoadComparisonRepository partLoadComparisonRepository) {
        this.partLoadComparisonRepository = partLoadComparisonRepository;
    }
}
