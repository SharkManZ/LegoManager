package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.UserSetDto;
import ru.shark.home.legomanager.datamanager.UserSetsDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class UserSetsService extends BaseLogicService {

    private UserSetsDataManager userSetsDataManager;

    public BaseResponse getList(Long userId, PageRequest request) {
        BaseResponse response;
        try {
            response = new BaseResponse<>();
            response.setBody(userSetsDataManager.getWithPagination(userId, getCriteria(request, UserSetDto.class)));
            response.setSuccess(true);

        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка наборов владельцев: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse save(UserSetDto dto) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(userSetsDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении набора владельца:" + e.getMessage());
        }

        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            userSetsDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении набора владельца: " + e.getMessage());
        }

        return response;
    }

    @Autowired
    public void setUserSetsDataManager(UserSetsDataManager userSetsDataManager) {
        this.userSetsDataManager = userSetsDataManager;
    }
}
