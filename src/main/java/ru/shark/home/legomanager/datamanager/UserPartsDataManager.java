package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.UserPartDto;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.dao.entity.UserPartEntity;
import ru.shark.home.legomanager.dao.service.UserPartsDao;

@Component
public class UserPartsDataManager extends BaseDataManager<UserPartEntity, UserPartDto> {
    protected UserPartsDataManager(UserPartsDao dao) {
        super(dao, UserPartDto.class);
    }

    public PageableList<UserPartListDto> getList(Long userId, RequestCriteria request) {
        UserPartsDao dao = (UserPartsDao) getDao();
        return dao.getList(userId, request);
    }
}
