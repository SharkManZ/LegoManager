package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.dao.dto.PartFullDto;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.service.PartDao;

@Component
public class PartDataManager extends BaseDataManager<PartEntity, PartDto> {
    protected PartDataManager(PartDao dao) {
        super(dao, PartDto.class);
    }

    public PageableList<PartFullDto> getWithPagination(RequestCriteria request) {
        PartDao dao = (PartDao) getDao();
        return dao.getWithPagination(request);
    }

    public PartDto savePart(PartDto dto) {
        PartDao dao = (PartDao) getDao();
        return dao.savePart(dto);
    }
}
