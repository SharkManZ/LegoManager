package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.entity.BaseEntity;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.dao.dto.SetFullDto;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.repository.SetRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class SetDao extends BaseDao<SetEntity> {
    private static String NAME_FIELD = "name";
    private static String NUMBER_FIELD = "number";

    private SetRepository setRepository;

    public SetDao() {
        super(SetEntity.class);
    }

    public PageableList<SetFullDto> getWithPagination(RequestCriteria request) {
        Specification<BaseEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(),
                NAME_FIELD, NUMBER_FIELD);
        return getListWithAdditionalFields(setRepository.getWithPagination(request, searchSpec, NUMBER_FIELD));
    }

    private PageableList<SetFullDto> getListWithAdditionalFields(PageableList<SetEntity> list) {
        List<SetFullDto> dtoList = new ArrayList<>();
        for (SetEntity entity : list.getData()) {
            SetFullDto dto = new SetFullDto();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setNumber(entity.getNumber());
            dto.setPartsCount(0);
            dto.setYear(entity.getYear());
            dto.setSeries(new SeriesDto());
            dto.getSeries().setId(entity.getSeries().getId());

            dtoList.add(dto);
        }
        return new PageableList<>(dtoList, list.getTotalCount());
    }

    @Autowired
    public void setSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
    }
}
