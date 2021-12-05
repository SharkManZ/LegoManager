package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.dto.PartCategoryDto;
import ru.shark.home.legomanager.dao.dto.PartFullDto;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.repository.PartCategoryRepository;
import ru.shark.home.legomanager.dao.repository.PartRepository;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.shark.home.common.common.ErrorConstants.*;

@Component
public class PartDao extends BaseDao<PartEntity> {
    private static String NAME_FIELD = "name";
    private static String NUMBER_FIELD = "number";
    private static String ALTERNATE_NUMBER_FIELD = "alternateNumber";

    private PartRepository partRepository;
    private PartCategoryRepository partCategoryRepository;

    public PartDao() {
        super(PartEntity.class);
    }

    public PageableList<PartFullDto> getWithPagination(RequestCriteria request) {
        Specification<PartEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(),
                NAME_FIELD, NUMBER_FIELD, ALTERNATE_NUMBER_FIELD);
        return getListWithAdditionalFields(partRepository.getWithPagination(request, searchSpec, NUMBER_FIELD));
    }

    private PageableList<PartFullDto> getListWithAdditionalFields(PageableList<PartEntity> list) {
        List<PartFullDto> dtoList = new ArrayList<>();
        List<Map<String, Object>> partColorsCountByIds = new ArrayList<>();
        if (!isEmpty(list.getData())) {
            partColorsCountByIds = partRepository.getPartAdditionalDataByIds(list.getData().stream().map(entity -> entity.getId()).collect(Collectors.toList()));
        }
        for (PartEntity entity : list.getData()) {
            PartFullDto dto = new PartFullDto();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setNumber(entity.getNumber());
            dto.setAlternateNumber(entity.getAlternateNumber());
            dto.setColorsCount(((Long) partColorsCountByIds.stream()
                    .filter(item -> item.get("id").equals(dto.getId()))
                    .findFirst()
                    .map(item -> item.get("cnt")).orElse(0L)).intValue());
            dto.setMinColorNumber((String) partColorsCountByIds.stream()
                    .filter(item -> item.get("id").equals(dto.getId()))
                    .findFirst()
                    .map(item -> item.get("minColorNumber")).orElse(null));
            dto.setCategory(new PartCategoryDto());
            dto.getCategory().setId(entity.getCategory().getId());
            dto.getCategory().setName(entity.getCategory().getName());

            dtoList.add(dto);
        }
        return new PageableList<>(dtoList, list.getTotalCount());
    }

    @Override
    public PartEntity save(PartEntity entity) {
        if (entity == null) {
            return null;
        }
        validateFields(entity);
        PartEntity setByNumber = partRepository.findPartByNumber(entity.getNumber());
        if (setByNumber != null && (entity.getId() == null || !entity.getId().equals(setByNumber.getId()))) {
            throw new ValidationException(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartEntity.getDescription(),
                    entity.getNumber()));
        }
        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        partRepository.findById(id).orElseThrow(() -> new ValidationException(MessageFormat.format(
                ENTITY_NOT_FOUND_BY_ID, PartEntity.getDescription(), id)));

        super.deleteById(id);
    }

    private void validateFields(PartEntity partEntity) {
        if (isBlank(partEntity.getNumber())) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                    PartEntity.getDescription()));
        }
        if (isBlank(partEntity.getName())) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "name",
                    PartEntity.getDescription()));
        }
        if (partEntity.getCategory() == null || partEntity.getCategory().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "category",
                    PartEntity.getDescription()));
        }
        partCategoryRepository.findById(partEntity.getCategory().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartCategoryEntity.getDescription(),
                        partEntity.getCategory().getId())));
    }

    @Autowired
    public void setPartRepository(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Autowired
    public void setPartCategoryRepository(PartCategoryRepository partCategoryRepository) {
        this.partCategoryRepository = partCategoryRepository;
    }
}
