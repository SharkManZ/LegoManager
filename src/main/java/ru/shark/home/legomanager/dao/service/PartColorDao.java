package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.repository.ColorRepository;
import ru.shark.home.legomanager.dao.repository.PartColorRepository;
import ru.shark.home.legomanager.dao.repository.PartRepository;
import ru.shark.home.legomanager.services.dto.PartColorSearchDto;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.shark.home.common.common.ErrorConstants.*;

@Component
public class PartColorDao extends BaseDao<PartColorEntity> {

    private PartColorRepository partColorRepository;
    private PartRepository partRepository;
    private ColorRepository colorRepository;

    public PartColorDao() {
        super(PartColorEntity.class);
    }

    public List<PartColorEntity> getPartColorListByPartId(Long partId) {
        return partColorRepository.getPartColorsByPartId(partId);
    }

    public PartColorEntity save(PartColorEntity entity) {
        if (entity == null) {
            return null;
        }
        validateFields(entity);
        PartColorEntity byNumber = partColorRepository.getPartColorByNumber(entity.getNumber());
        if (byNumber != null && (entity.getId() == null || entity.getId() != byNumber.getId())) {
            throw new ValidationException(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartColorEntity.getDescription(),
                    entity.getNumber()));
        }

        PartColorEntity byPartAndColor = partColorRepository.getPartColorByPartAndColor(entity.getPart().getId(),
                entity.getColor().getId());
        if (byPartAndColor != null && (entity.getId() == null || entity.getId() != byPartAndColor.getId())) {
            throw new ValidationException(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartColorEntity.getDescription(),
                    entity.getPart().getId() + " " + entity.getColor().getId()));
        }

        return super.save(entity);
    }

    private void validateFields(PartColorEntity partColorEntity) {
        if (isBlank(partColorEntity.getNumber())) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                    PartColorEntity.getDescription()));
        }
        if (partColorEntity.getPart() == null || partColorEntity.getPart().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "part",
                    PartColorEntity.getDescription()));
        }
        partRepository.findById(partColorEntity.getPart().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartEntity.getDescription(),
                        partColorEntity.getPart().getId())));
        if (partColorEntity.getColor() == null || partColorEntity.getColor().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "color",
                    PartColorEntity.getDescription()));
        }
        colorRepository.findById(partColorEntity.getColor().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, ColorEntity.getDescription(),
                        partColorEntity.getColor().getId())));
    }

    public PartColorEntity search(PartColorSearchDto dto) {
        return partColorRepository.getPartColorByNumber(dto.getSearchValue());
    }

    @Autowired
    public void setPartColorRepository(PartColorRepository partColorRepository) {
        this.partColorRepository = partColorRepository;
    }

    @Autowired
    public void setPartRepository(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Autowired
    public void setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }
}
