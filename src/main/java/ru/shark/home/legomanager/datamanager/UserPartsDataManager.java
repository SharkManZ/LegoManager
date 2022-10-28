package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.UserPartDto;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.dao.entity.UserPartEntity;
import ru.shark.home.legomanager.dao.service.UserPartsDao;

import java.util.List;

@Component
public class UserPartsDataManager extends BaseDataManager<UserPartEntity, UserPartDto> {
    protected UserPartsDataManager(UserPartsDao dao) {
        super(dao, UserPartDto.class);
    }

    public List<UserPartListDto> getList(Long userId) {
        UserPartsDao dao = (UserPartsDao) getDao();
        return dao.getList(userId);
    }
}
