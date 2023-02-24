package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.datamanager.PartLoadSkipDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class DictionaryService extends BaseLogicService {
    private PartLoadSkipDataManager partLoadSkipDataManager;

    public BaseResponse getPartLoadSkipList(PageRequest request) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(partLoadSkipDataManager.getWithPagination(getCriteria(request, PartLoadSkipDto.class)));
            response.setSuccess(true);
        } catch (Exception ex) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка шаблонов пропуска деталей импорта: " + ex.getMessage());
        }
        return response;
    }

    public BaseResponse savePartLoadSkip(PartLoadSkipDto dto) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(partLoadSkipDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception ex) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении шаблона пропуска деталей импорта: " + ex.getMessage());
        }
        return response;
    }

    public BaseResponse deletePartLoadSkip(Long id) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            partLoadSkipDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception ex) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении шаблона пропуска деталей импорта: " + ex.getMessage());
        }
        return response;
    }

    @Autowired
    public void setPartLoadSkipDataManager(PartLoadSkipDataManager partLoadSkipDataManager) {
        this.partLoadSkipDataManager = partLoadSkipDataManager;
    }
}
