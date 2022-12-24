package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.datamanager.PartColorDataManager;
import ru.shark.home.legomanager.services.dto.SearchDto;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class PartColorService extends BaseLogicService {

    private PartColorDataManager partColorDataManager;

    public BaseResponse getListByPart(Long partId, Search search) {
        BaseResponse baseResponse;
        try {
            baseResponse = new BaseResponse();
            baseResponse.setBody(partColorDataManager.getPartColorListByPartId(partId, search));
            baseResponse.setSuccess(true);
        } catch (Exception ex) {
            baseResponse = BaseResponse.buildError(ERR_500, "Ошибка при получении цветов деталей: " + ex.getMessage());
        }

        return baseResponse;
    }

    public BaseResponse save(PartColorDto dto) {
        BaseResponse baseResponse;
        try {
            baseResponse = new BaseResponse();
            baseResponse.setBody(partColorDataManager.savePartColor(dto));
            baseResponse.setSuccess(true);
        } catch (Exception e) {
            baseResponse = BaseResponse.buildError(ERR_500, "Ошибка при сохранении цвета детали: " + e.getMessage());
        }

        return baseResponse;
    }

    public BaseResponse delete(Long id) {
        BaseResponse baseResponse;
        try {
            baseResponse = new BaseResponse();
            partColorDataManager.deleteById(id);
            baseResponse.setSuccess(true);
        } catch (Exception e) {
            baseResponse = BaseResponse.buildError(ERR_500, "Ошибка при удалении цвета детали: " + e.getMessage());
        }

        return baseResponse;
    }

    public BaseResponse search(SearchDto dto) {
        BaseResponse baseResponse;
        try {
            baseResponse = new BaseResponse();
            baseResponse.setBody(partColorDataManager.search(dto));
            baseResponse.setSuccess(true);
        } catch (Exception e) {
            baseResponse = BaseResponse.buildError(ERR_500, "Ошибка при поиске цвета детали: " + e.getMessage());
        }
        return baseResponse;
    }

    @Autowired
    public void setPartColorDataManager(PartColorDataManager partColorDataManager) {
        this.partColorDataManager = partColorDataManager;
    }
}
