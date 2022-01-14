package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.SetDto;
import ru.shark.home.legomanager.dao.dto.SetFullDto;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.service.SetDao;

@Component
public class SetDataManager extends BaseDataManager<SetEntity, SetDto> {
    protected SetDataManager(SetDao dao) {
        super(dao, SetDto.class);
    }

    public PageableList<SetFullDto> getWithPagination(RequestCriteria request) {
        SetDao dao = (SetDao) getDao();
        return dao.getWithPagination(request);
    }

    public PageableList<SetFullDto> getWithPagination(RequestCriteria request, Long seriesId) {
        SetDao dao = (SetDao) getDao();
        return dao.getWithPagination(request, seriesId);
    }
}
