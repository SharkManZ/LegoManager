package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.PartCategoryDto;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.dao.service.PartCategoryDao;

@Component
public class PartCategoryDataManager extends BaseDataManager<PartCategoryEntity, PartCategoryDto> {
    public PartCategoryDataManager(PartCategoryDao partCategoryDao) {
        super(partCategoryDao, PartCategoryDto.class);
    }

    public PageableList<PartCategoryDto> getWithPagination(RequestCriteria request) {
        PartCategoryDao dao = (PartCategoryDao) getDao();
        return getConverterUtil().entityListToDtoPageableList(dao.getWithPagination(request), PartCategoryDto.class);
    }
}
