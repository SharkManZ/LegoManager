package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.UserPartDto;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.dao.dto.request.UserPartListRequest;
import ru.shark.home.legomanager.datamanager.UserPartsDataManager;
import ru.shark.home.legomanager.services.dto.UserPartRequestDto;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class UserPartsService extends BaseLogicService {
    private UserPartsDataManager userPartsDataManager;

    public BaseResponse getList(Long userId, UserPartRequestDto request) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(userPartsDataManager.getList(new UserPartListRequest(request.getRequestType(), userId),
                    getCriteria(request, UserPartListDto.class)));
            response.setSuccess(true);
        } catch (Exception ex) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка деталей владельца: " + ex.getMessage());
        }

        return response;
    }

    public BaseResponse save(UserPartDto dto) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(userPartsDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception ex) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении детали владельца: " + ex.getMessage());
        }

        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            userPartsDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception ex) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении детали владельца: " + ex.getMessage());
        }

        return response;
    }

    @Autowired
    public void setUserPartsDataManager(UserPartsDataManager userPartsDataManager) {
        this.userPartsDataManager = userPartsDataManager;
    }
}
