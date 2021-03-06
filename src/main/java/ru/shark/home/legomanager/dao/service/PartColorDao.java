package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.repository.ColorRepository;
import ru.shark.home.legomanager.dao.repository.PartColorRepository;
import ru.shark.home.legomanager.dao.repository.PartRepository;
import ru.shark.home.legomanager.services.dto.SearchDto;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.shark.home.common.common.ErrorConstants.*;
import static ru.shark.home.legomanager.common.ErrorConstants.MORE_THAN_ONE_PART_COLOR;

@Component
public class PartColorDao extends BaseDao<PartColorEntity> {

    private PartColorRepository partColorRepository;
    private PartRepository partRepository;
    private ColorRepository colorRepository;

    public PartColorDao() {
        super(PartColorEntity.class);
    }

    public List<PartColorEntity> getPartColorListByPartId(Long partId, Search search) {
        if (search == null || isBlank(search.getValue())) {
            return partColorRepository.getPartColorsByPartId(partId);
        }
        if (search.isEquals()) {
            return partColorRepository.getPartColorsByPartIdAndEqualsSearch(partId, search.getValue());
        } else {
            return partColorRepository.getPartColorsByPartIdAndNotEqualsSearch(partId, search.getValue());
        }
    }

    public PartColorEntity save(PartColorEntity entity) {
        if (entity == null) {
            return null;
        }
        validateFields(entity);

        PartColorEntity byPartAndColor = partColorRepository.getPartColorByPartAndColor(entity.getPart().getId(),
                entity.getColor().getId());
        if (byPartAndColor != null && (entity.getId() == null || !entity.getId().equals(byPartAndColor.getId()))) {
            throw new ValidationException(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                    PartColorEntity.getDescription(),
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

    public PartColorEntity search(SearchDto dto) {
        String[] searchParts = dto.getSearchValue().split(" ");
        if (searchParts.length == 1) {
            Long countByNumber = partColorRepository.getPartColorCountByNumber(searchParts[0]);
            if (countByNumber == 0) {
                return null;
            } else if (countByNumber == 1) {
                return partColorRepository.getPartColorByNumber(searchParts[0]);
            } else {
                throw new ValidationException(MessageFormat.format(MORE_THAN_ONE_PART_COLOR, countByNumber,
                        searchParts[0]));
            }
        } else {
            List<PartColorEntity> list = partColorRepository.getPartColorByNumberPartNumber(searchParts[0],
                    searchParts[1]);
            if (ObjectUtils.isEmpty(list)) {
                return null;
            }
            return list.stream().filter(item -> isEqualByAlternate(item, searchParts[0])).findFirst().orElse(null);
        }
    }

    private boolean isEqualByAlternate(PartColorEntity entity, String number) {
        if (entity.getNumber().equalsIgnoreCase(number)) {
            return true;
        }
        if (isBlank(entity.getAlternateNumber())) {
            return false;
        }
        long count = Arrays.stream(entity.getAlternateNumber().split(","))
                .filter(item -> item.trim().equalsIgnoreCase(number))
                .count();
        return count == 1;
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
