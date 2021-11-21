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
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.repository.SeriesRepository;
import ru.shark.home.legomanager.dao.repository.SetRepository;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.shark.home.common.common.ErrorConstants.*;

@Component
public class SetDao extends BaseDao<SetEntity> {
    private static String NAME_FIELD = "name";
    private static String NUMBER_FIELD = "number";

    private SetRepository setRepository;
    private SeriesRepository seriesRepository;

    public SetDao() {
        super(SetEntity.class);
    }

    public PageableList<SetFullDto> getWithPagination(RequestCriteria request) {
        Specification<SetEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(),
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
            dto.getSeries().setName(entity.getSeries().getName());

            dtoList.add(dto);
        }
        return new PageableList<>(dtoList, list.getTotalCount());
    }

    @Override
    public SetEntity save(SetEntity entity) {
        if (entity == null) {
            return null;
        }
        validateFields(entity);
        SetEntity setByNumber = setRepository.findSetByNumber(entity.getNumber());
        if (setByNumber != null && (entity.getId() == null || !entity.getId().equals(setByNumber.getId()))) {
            throw new ValidationException(MessageFormat.format(ENTITY_ALREADY_EXISTS, SetEntity.getDescription(),
                    entity.getNumber()));
        }
        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        setRepository.findById(id).orElseThrow(() -> new ValidationException(MessageFormat.format(
                ENTITY_NOT_FOUND_BY_ID, SetEntity.getDescription(), id)));

        super.deleteById(id);
    }

    private void validateFields(SetEntity setEntity) {
        if (isBlank(setEntity.getNumber())) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                    SetEntity.getDescription()));
        }
        if (isBlank(setEntity.getName())) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "name",
                    SetEntity.getDescription()));
        }
        if (setEntity.getYear() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "year",
                    SetEntity.getDescription()));
        }
        if (setEntity.getSeries() == null || setEntity.getSeries().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "series",
                    SetEntity.getDescription()));
        }
        seriesRepository.findById(setEntity.getSeries().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, SeriesEntity.getDescription(),
                        setEntity.getSeries().getId())));
    }

    @Autowired
    public void setSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @Autowired
    public void setSeriesRepository(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }
}
