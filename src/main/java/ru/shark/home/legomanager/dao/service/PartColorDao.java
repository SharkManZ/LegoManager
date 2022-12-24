package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.legomanager.dao.dto.NumberDto;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorNumberEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.repository.ColorRepository;
import ru.shark.home.legomanager.dao.repository.PartColorRepository;
import ru.shark.home.legomanager.dao.repository.PartRepository;
import ru.shark.home.legomanager.services.dto.SearchDto;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.shark.home.common.common.ErrorConstants.*;
import static ru.shark.home.legomanager.common.ErrorConstants.MORE_THAN_ONE_PART_COLOR;

@Component
public class PartColorDao extends BaseDao<PartColorEntity> {

    private PartColorRepository partColorRepository;
    private PartRepository partRepository;
    private ColorRepository colorRepository;
    private PartColorNumberDao partColorNumberDao;

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

    public PartColorDto savePartColor(PartColorDto dto) {
        if (dto == null) {
            return null;
        }
        validateFields(dto);
        List<NumberDto> numbersListDto = mapNumbersToList(dto);
        List<String> numbers = numbersListDto.stream()
                .map(item -> item.getNumber().toLowerCase()).collect(Collectors.toList());
        PartColorEntity byPartAndColor = partColorRepository.getPartColorByPartAndColor(dto.getPart().getId(),
                dto.getColor().getId());
        if (byPartAndColor != null && (dto.getId() == null || !dto.getId().equals(byPartAndColor.getId()))) {
            throw new ValidationException(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                    PartColorEntity.getDescription(),
                    dto.getPart().getId() + " " + dto.getColor().getId()));
        }
        PartColorEntity saved = super.save(getConverterUtil().dtoToEntity(dto, PartColorEntity.class));
        PartColorDto result = getConverterUtil().entityToDto(saved, PartColorDto.class);
        List<PartColorNumberEntity> numberDtos = partColorNumberDao.savePartColorNumbers(saved, numbersListDto);
        result.setNumber(numberDtos.stream().filter(PartColorNumberEntity::getMain).findFirst()
                .orElseThrow(() -> new ValidationException("Не найден основной номер детали")).getNumber());
        if (numberDtos.size() > 1) {
            result.setAlternateNumber(numberDtos.stream()
                    .filter(item -> !item.getMain())
                    .map(item -> item.getNumber())
                    .collect(Collectors.joining(", ")));
        }
        return result;
    }

    private List<NumberDto> mapNumbersToList(PartColorDto dto) {
        List<NumberDto> result = new ArrayList<>();
        result.add(new NumberDto(dto.getNumber(), true));
        if (!isBlank(dto.getAlternateNumber())) {
            result.addAll(Stream.of(dto.getAlternateNumber().split(","))
                    .map(item -> new NumberDto(item.trim(), false))
                    .collect(Collectors.toList()));
        }
        return result;
    }

    private void validateFields(PartColorDto dto) {
        if (isBlank(dto.getNumber())) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                    PartColorEntity.getDescription()));
        }
        if (dto.getPart() == null || dto.getPart().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "part",
                    PartColorEntity.getDescription()));
        }
        partRepository.findById(dto.getPart().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartEntity.getDescription(),
                        dto.getPart().getId())));
        if (dto.getColor() == null || dto.getColor().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "color",
                    PartColorEntity.getDescription()));
        }
        colorRepository.findById(dto.getColor().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, ColorEntity.getDescription(),
                        dto.getColor().getId())));
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
        return entity.getNumbers().stream().filter(item -> item.getNumber().equalsIgnoreCase(number))
                .count() == 1;
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

    @Autowired
    public void setPartColorNumberDao(PartColorNumberDao partColorNumberDao) {
        this.partColorNumberDao = partColorNumberDao;
    }
}
