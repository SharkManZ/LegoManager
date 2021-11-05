package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.dao.service.SeriesDao;

@Component
public class SeriesDataManager extends BaseDataManager<SeriesEntity, SeriesDto> {
    protected SeriesDataManager(SeriesDao dao) {
        super(dao, SeriesDto.class);
    }

    public PageableList<SeriesDto> getWithPagination(RequestCriteria request) {
        SeriesDao dao = (SeriesDao) getDao();
        return getConverterUtil().entityListToDtoPageableList(dao.getWithPagination(request), SeriesDto.class);
    }
}
