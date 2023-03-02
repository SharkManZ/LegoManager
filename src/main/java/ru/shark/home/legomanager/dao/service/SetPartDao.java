package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.legomanager.dao.dto.SetPartFullDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.entity.SetPartEntity;
import ru.shark.home.legomanager.dao.repository.PartColorRepository;
import ru.shark.home.legomanager.dao.repository.SetPartRepository;
import ru.shark.home.legomanager.dao.repository.SetRepository;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_EMPTY_FIELD;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_FIELD_VALUE_LOWER;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;

@Component
public class SetPartDao extends BaseDao<SetPartEntity> {

    private SetPartRepository setPartRepository;
    private SetRepository setRepository;
    private PartColorRepository partColorRepository;

    public SetPartDao() {
        super(SetPartEntity.class);
    }

    public PageableList<SetPartFullDto> getPartsBySetId(Long setId, RequestCriteria requestCriteria) {
        Map<String, Object> params = new HashMap<>();
        params.put("setId", setId);
        return setPartRepository.getNativeWithPagination("getSetPartsBySetId",
                requestCriteria, params,
                Arrays.asList("colorNumber", "alternateColorNumber", "number",
                        "alternateNumber", "partName", "colorName"), "getSetPartsBySetIdColumns");
    }

    @Override
    public SetPartEntity save(SetPartEntity entity) {
        if (entity == null) {
            return entity;
        }
        validateFields(entity);
        SetPartEntity bySetAndPartColor = setPartRepository.getSetPartBySetAndPrtColorId(entity.getSet().getId(),
                entity.getPartColor().getId());
        if (bySetAndPartColor != null && (entity.getId() == null || !entity.getId().equals(bySetAndPartColor.getId()))) {
            throw new ValidationException(MessageFormat.format(ENTITY_ALREADY_EXISTS, SetPartEntity.getDescription(),
                    entity.getSet().getId() + " " + entity.getPartColor().getId()));
        }
        return super.save(entity);
    }

    private void validateFields(SetPartEntity entity) {
        if (entity.getCount() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "count",
                    SetPartEntity.getDescription()));
        }
        if (entity.getCount() <= 0) {
            throw new ValidationException(MessageFormat.format(ENTITY_FIELD_VALUE_LOWER, "count", 0));
        }
        if (entity.getSet() == null || entity.getSet().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "set",
                    SetPartEntity.getDescription()));
        }
        setRepository.findById(entity.getSet().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, SetEntity.getDescription(),
                        entity.getSet().getId())));
        if (entity.getPartColor() == null || entity.getPartColor().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "partColor",
                    SetPartEntity.getDescription()));
        }
        partColorRepository.findById(entity.getPartColor().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartColorEntity.getDescription(),
                        entity.getPartColor().getId())));
    }

    @Autowired
    public void setSetPartRepository(SetPartRepository setPartRepository) {
        this.setPartRepository = setPartRepository;
    }

    @Autowired
    public void setSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @Autowired
    public void setPartColorRepository(PartColorRepository partColorRepository) {
        this.partColorRepository = partColorRepository;
    }
}
