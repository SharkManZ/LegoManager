package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.datamanager.ColorDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class ColorService extends BaseLogicService {

    private ColorDataManager colorDataManager;

    public BaseResponse getList(PageRequest request) {
        BaseResponse response;
        try {
            response = new BaseResponse<>();
            response.setBody(colorDataManager.getWithPagination(getCriteria(request, ColorDto.class)));
            response.setSuccess(true);

        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка цветов: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse getAllList() {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(colorDataManager.getAllColors());
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении полного списка цветов: " + e.getMessage());
        }
        return response;
    }

    public BaseResponse save(ColorDto dto) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(colorDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении цвета:" + e.getMessage());
        }

        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            colorDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении цвета: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse getPartNotExistsColors(Long partId) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(colorDataManager.getPartNotExistsColors(partId));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении отсутствующих цветов детали: " +
                    e.getMessage());
        }

        return response;
    }

    @Autowired
    public void setColorDataManager(ColorDataManager colorDataManager) {
        this.colorDataManager = colorDataManager;
    }
}
