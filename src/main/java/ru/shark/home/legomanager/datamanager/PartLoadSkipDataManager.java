package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.dao.entity.load.PartLoadSkipEntity;
import ru.shark.home.legomanager.dao.service.PartLoadSkipDao;

@Component
public class PartLoadSkipDataManager extends BaseDataManager<PartLoadSkipEntity, PartLoadSkipDto> {
    protected PartLoadSkipDataManager(PartLoadSkipDao dao) {
        super(dao, PartLoadSkipDto.class);
    }

    public PageableList<PartLoadSkipDto> getWithPagination(RequestCriteria requestCriteria) {
        PartLoadSkipDao dao = (PartLoadSkipDao) getDao();
        return getConverterUtil().entityListToDtoPageableList(dao.getWithPagination(requestCriteria),
                PartLoadSkipDto.class);
    }
}
