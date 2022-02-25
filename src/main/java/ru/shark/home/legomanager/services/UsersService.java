package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.UserDto;
import ru.shark.home.legomanager.datamanager.UsersDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class UsersService extends BaseLogicService {

    private UsersDataManager usersDataManager;

    public BaseResponse getList(PageRequest request) {
        BaseResponse response;
        try {
            response = new BaseResponse<>();
            response.setBody(usersDataManager.getWithPagination(getCriteria(request, UserDto.class)));
            response.setSuccess(true);

        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка владельцев: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse getAllList() {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(usersDataManager.getAllUsers());
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении полного списка владельцев: " + e.getMessage());
        }
        return response;
    }

    public BaseResponse save(UserDto dto) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(usersDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении владельца:" + e.getMessage());
        }

        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            usersDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении владельца: " + e.getMessage());
        }

        return response;
    }

    @Autowired
    public void setUsersDataManager(UsersDataManager usersDataManager) {
        this.usersDataManager = usersDataManager;
    }
}
