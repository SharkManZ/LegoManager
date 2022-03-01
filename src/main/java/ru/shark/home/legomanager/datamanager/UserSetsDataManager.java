package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.UserSetDto;
import ru.shark.home.legomanager.dao.entity.UserSetEntity;
import ru.shark.home.legomanager.dao.service.UserSetsDao;

@Component
public class UserSetsDataManager extends BaseDataManager<UserSetEntity, UserSetDto> {
    protected UserSetsDataManager(UserSetsDao dao) {
        super(dao, UserSetDto.class);
    }

    public PageableList<UserSetDto> getWithPagination(Long userId, RequestCriteria requestCriteria) {
        UserSetsDao dao = (UserSetsDao) getDao();
        return getConverterUtil().entityListToDtoPageableList(dao.getWithPagination(userId, requestCriteria),
                UserSetDto.class);
    }
}
