package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.service.ColorDao;

@Component
public class ColorDataManager extends BaseDataManager<ColorEntity, ColorDto> {
    public ColorDataManager(ColorDao colorDao) {
        super(colorDao, ColorDto.class);
    }

    public PageableList<ColorDto> getWithPagination(RequestCriteria request) {
        ColorDao dao = (ColorDao) getDao();
        return getConverterUtil().entityListToDtoPageableList(dao.getWithPagination(request), ColorDto.class);
    }
}
