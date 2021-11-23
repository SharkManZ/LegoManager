package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.datamanager.PartDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class PartService extends BaseLogicService {
    private PartDataManager partDataManager;

    public BaseResponse getList(PageRequest request) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(partDataManager.getWithPagination(getCriteria(request, PartDto.class)));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка деталей: " +
                    e.getMessage());
        }

        return response;
    }

    public BaseResponse save(PartDto dto) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(partDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении детали: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            partDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении детали: " + e.getMessage());
        }

        return response;
    }

    @Autowired
    public void setPartDataManager(PartDataManager partDataManager) {
        this.partDataManager = partDataManager;
    }
}
