package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.ListRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.SetPartDto;
import ru.shark.home.legomanager.datamanager.SetPartDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class SetPartService extends BaseLogicService {

    private SetPartDataManager setPartDataManager;

    public BaseResponse getListBySetId(Long setId, ListRequest request) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(setPartDataManager.getPartsBySetId(setId, request));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка деталей набора: " + e.getMessage());
        }
        return response;
    }

    public BaseResponse save(SetPartDto dto) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(setPartDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении детали набора: " + e.getMessage());
        }
        return response;
    }

    public BaseResponse deleteById(Long id) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            setPartDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении детали набора: " + e.getMessage());
        }
        return response;
    }

    @Autowired
    public void setSetPartDataManager(SetPartDataManager setPartDataManager) {
        this.setPartDataManager = setPartDataManager;
    }
}
