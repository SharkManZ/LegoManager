package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.SetPartDto;
import ru.shark.home.legomanager.dao.dto.SetPartFullDto;
import ru.shark.home.legomanager.dao.entity.SetPartEntity;
import ru.shark.home.legomanager.dao.service.SetPartDao;

@Component
public class SetPartDataManager extends BaseDataManager<SetPartEntity, SetPartDto> {
    public SetPartDataManager(SetPartDao dao) {
        super(dao, SetPartDto.class);
    }

    public PageableList<SetPartFullDto> getPartsBySetId(Long setId, RequestCriteria request) {
        SetPartDao dao = (SetPartDao) getDao();
        return dao.getPartsBySetId(setId, request);
    }
}
