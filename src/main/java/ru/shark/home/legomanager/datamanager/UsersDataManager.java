package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.UserDto;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.dao.service.UsersDao;

import java.util.List;

@Component
public class UsersDataManager extends BaseDataManager<UserEntity, UserDto> {
    public UsersDataManager(UsersDao usersDao) {
        super(usersDao, UserDto.class);
    }

    public PageableList<UserDto> getWithPagination(RequestCriteria request) {
        UsersDao dao = (UsersDao) getDao();
        return getConverterUtil().entityListToDtoPageableList(dao.getWithPagination(request), UserDto.class);
    }

    public List<UserDto> getAllUsers() {
        UsersDao dao = (UsersDao) getDao();
        return getConverterUtil().entityListToDtoList(dao.getAllUsers(), UserDto.class);
    }
}
