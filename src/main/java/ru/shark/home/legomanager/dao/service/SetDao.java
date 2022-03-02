package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.dao.dto.SetFullDto;
import ru.shark.home.legomanager.dao.dto.SetSummaryDto;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.repository.ColorRepository;
import ru.shark.home.legomanager.dao.repository.SeriesRepository;
import ru.shark.home.legomanager.dao.repository.SetRepository;
import ru.shark.home.legomanager.services.dto.SearchDto;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.shark.home.common.common.ErrorConstants.*;
import static ru.shark.home.common.dao.util.SpecificationUtils.andSpecifications;

@Component
public class SetDao extends BaseDao<SetEntity> {
    private static String NAME_FIELD = "name";
    private static String NUMBER_FIELD = "number";

    private SetRepository setRepository;
    private SeriesRepository seriesRepository;
    private ColorRepository colorRepository;

    public SetDao() {
        super(SetEntity.class);
    }

    public PageableList<SetFullDto> getWithPagination(RequestCriteria request) {
        return getWithPagination(request, null);
    }

    public PageableList<SetFullDto> getWithPagination(RequestCriteria request, Long seriesId) {
        Specification<SetEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(),
                NAME_FIELD, NUMBER_FIELD);
        return getListWithAdditionalFields(setRepository.getWithPagination(request,
                seriesId == null ? searchSpec : andSpecifications(searchSpec,
                        SpecificationUtils.equalAttribute("series.id", seriesId)),
                NUMBER_FIELD));
    }

    private PageableList<SetFullDto> getListWithAdditionalFields(PageableList<SetEntity> list) {
        List<SetFullDto> dtoList = new ArrayList<>();
        List<Map<String, Object>> additionalData = null;
        if (list.getData().size() > 0) {
            additionalData = setRepository.getSetsAdditionalData(list.getData().stream()
                    .map(SetEntity::getId).collect(Collectors.toList()));
        }
        for (SetEntity entity : list.getData()) {
            SetFullDto dto = new SetFullDto();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setNumber(entity.getNumber());
            Map<String, Object> additional = additionalData.stream()
                    .filter(item -> item.get("id").equals(entity.getId())).findFirst().orElse(null);
            dto.setPartsCount(additional != null && additional.get("partsCount") != null ?
                    ((Long) additional.get("partsCount")).intValue() : 0);
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

    public SetSummaryDto getSummary(Long id) {
        setRepository.findById(id).orElseThrow(() -> new ValidationException(MessageFormat.format(
                ENTITY_NOT_FOUND_BY_ID, SetEntity.getDescription(), id)));
        Map<String, Object> summary = setRepository.getSetSummary(id);
        List<ColorEntity> colors = colorRepository.getColorsBySetId(id);

        SetSummaryDto dto = new SetSummaryDto();
        dto.setNumber((String) summary.get("number"));
        dto.setName((String) summary.get("name"));
        dto.setYear((Integer) summary.get("year"));
        Long partsCount = (Long) summary.get("partsCount");
        dto.setPartsCount(partsCount != null ? partsCount.intValue() : 0);
        Long uniquePartsCount = (Long) summary.get("uniquePartsCount");
        dto.setUniquePartsCount(uniquePartsCount != null ? uniquePartsCount.intValue() : 0);
        if (!isEmpty(colors)) {
            dto.setColors(colors.stream().map(item -> new ColorDto(item.getName(), item.getHexColor()))
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public SetEntity search(SearchDto dto) {
        return setRepository.findSetByNumber(dto.getSearchValue());
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

    @Autowired
    public void setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }
}
