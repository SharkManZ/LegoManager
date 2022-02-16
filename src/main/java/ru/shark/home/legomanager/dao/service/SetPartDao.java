package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.services.dto.ListRequest;
import ru.shark.home.legomanager.dao.dto.SetPartFullDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.entity.SetPartEntity;
import ru.shark.home.legomanager.dao.repository.PartColorRepository;
import ru.shark.home.legomanager.dao.repository.SetPartRepository;
import ru.shark.home.legomanager.dao.repository.SetRepository;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.shark.home.common.common.ErrorConstants.*;

@Component
public class SetPartDao extends BaseDao<SetPartEntity> {

    private SetPartRepository setPartRepository;
    private SetRepository setRepository;
    private PartColorRepository partColorRepository;

    public SetPartDao() {
        super(SetPartEntity.class);
    }

    public List<SetPartFullDto> getPartsBySetId(Long setId, ListRequest request) {
        String search = request != null && request.getSearch() != null ? request.getSearch().getValue() : "";
        List<SetPartEntity> entityList = setPartRepository.getSetPartsBySetId(setId, search);
        if (ObjectUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        List<SetPartFullDto> list = new ArrayList<>();
        for (SetPartEntity entity : entityList) {
            SetPartFullDto dto = new SetPartFullDto();
            dto.setId(entity.getId());
            dto.setCount(entity.getCount());
            dto.setSetId(entity.getSet().getId());
            dto.setPartColorId(entity.getPartColor().getId());
            dto.setNumber(entity.getPartColor().getPart().getNumber());
            if (!isBlank(entity.getPartColor().getPart().getAlternateNumber())) {
                dto.setAlternateNumber(entity.getPartColor().getPart().getAlternateNumber());
            }
            dto.setColorNumber(entity.getPartColor().getNumber());
            if (!isBlank(entity.getPartColor().getAlternateNumber())) {
                dto.setAlternateColorNumber(entity.getPartColor().getAlternateNumber());
            }
            dto.setHexColor(entity.getPartColor().getColor().getHexColor());
            dto.setPartName(entity.getPartColor().getPart().getName());

            list.add(dto);
        }
        return list;
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
