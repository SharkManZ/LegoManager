package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.common.RequestSort;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.dao.dto.NumberDto;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.dao.dto.PartFullDto;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.entity.PartNumberEntity;
import ru.shark.home.legomanager.dao.repository.PartCategoryRepository;
import ru.shark.home.legomanager.dao.repository.PartColorRepository;
import ru.shark.home.legomanager.dao.repository.PartNumberRepository;
import ru.shark.home.legomanager.dao.repository.PartRepository;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.shark.home.common.common.ErrorConstants.*;

@Component
public class PartDao extends BaseDao<PartEntity> {
    private static String NAME_FIELD = "name";
    private static String NUMBER_FIELD = "number";
    private static String ALTERNATE_NUMBER_FIELD_SEARCH = "exists (select 1 from lego_part_number lpn " +
            "where lpn.lego_part_id = a.id and lpn.lego_number {0})";

    private PartRepository partRepository;
    private PartCategoryRepository partCategoryRepository;
    private PartColorRepository partColorRepository;
    private PartNumberDao partNumberDao;
    private PartNumberRepository partNumberRepository;

    public PartDao() {
        super(PartEntity.class);
    }

    public PageableList<PartFullDto> getWithPagination(RequestCriteria request) {
        List<String> searchFields = Arrays.asList(NAME_FIELD);
        List<String> advancedSearchFields = Arrays.asList(ALTERNATE_NUMBER_FIELD_SEARCH);
        if (isEmpty(request.getSorts())) {
            request.setSorts(Arrays.asList(new RequestSort(NUMBER_FIELD, null)));
        }
        return getListWithAdditionalFields(partRepository.getNativeWithPagination("getParts", request,
                null, searchFields, advancedSearchFields, "getPartsColumns"));
    }

    private PageableList<PartFullDto> getListWithAdditionalFields(PageableList<PartFullDto> list) {
        List<PartFullDto> dtoList = new ArrayList<>();
        List<PartColorEntity> colors = new ArrayList<>();
        if (!isEmpty(list.getData())) {
            List<Long> ids = list.getData().stream().map(entity -> entity.getId()).collect(Collectors.toList());
            colors = partColorRepository.getPartColorsByPartIds(ids);
        }
        for (PartFullDto dto : list.getData()) {
            dto.setColors(getPartColorNumbers(dto.getId(), colors));

            dtoList.add(dto);
        }
        return new PageableList<>(dtoList, list.getTotalCount());
    }

    private List<ColorDto> getPartColorNumbers(Long partId, List<PartColorEntity> colors) {
        if (isEmpty(colors)) {
            return Collections.emptyList();
        }

        return colors.stream()
                .filter(item -> item.getPart().getId().equals(partId))
                .map(item -> new ColorDto(item.getColor().getName(), item.getColor().getHexColor()))
                .collect(Collectors.toList());
    }

    public PartDto savePart(PartDto dto) {
        if (dto == null) {
            return null;
        }
        validateFields(dto);
        List<NumberDto> numbersListDto = mapNumbersToList(dto);
        List<String> numbers = numbersListDto.stream()
                .map(item -> item.getNumber().toLowerCase()).collect(Collectors.toList());
        List<Long> partIdsByNumbers = partRepository.getPartIdsByNumbers(numbers);
        if (!isEmpty(partIdsByNumbers)) {
            if (partIdsByNumbers.size() > 1) {
                throw new ValidationException(MessageFormat.format("Найдено несколько деталей с номерами {0}", numbers));
            } else if (dto.getId() == null || !dto.getId().equals(partIdsByNumbers.get(0))) {
                throw new ValidationException(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartEntity.getDescription(),
                        numbers));
            }
        }
        PartEntity entity = getConverterUtil().dtoToEntity(dto, PartEntity.class);
        entity = super.save(entity);
        PartDto result = getConverterUtil().entityToDto(entity, PartDto.class);
        List<PartNumberEntity> numberDtos = partNumberDao.savePartNumbers(entity, numbersListDto);
        result.setNumber(numberDtos.stream().filter(PartNumberEntity::getMain).findFirst()
                .orElseThrow(() -> new ValidationException("Не найден основной номер детали")).getNumber());
        if (numberDtos.size() > 1) {
            result.setAlternateNumber(numberDtos.stream()
                    .filter(item -> !item.getMain())
                    .map(item -> item.getNumber())
                    .collect(Collectors.joining(", ")));
        }
        return result;
    }

    private List<NumberDto> mapNumbersToList(PartDto dto) {
        List<NumberDto> result = new ArrayList<>();
        result.add(new NumberDto(dto.getNumber(), true));
        if (!isBlank(dto.getAlternateNumber())) {
            result.addAll(Stream.of(dto.getAlternateNumber().split(","))
                    .map(item -> new NumberDto(item.trim(), false))
                    .collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public void deleteById(Long id) {
        partRepository.findById(id).orElseThrow(() -> new ValidationException(MessageFormat.format(
                ENTITY_NOT_FOUND_BY_ID, PartEntity.getDescription(), id)));

        super.deleteById(id);
    }

    private void validateFields(PartDto dto) {
        if (isEmpty(dto.getNumber())) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                    PartEntity.getDescription()));
        }
        if (isBlank(dto.getName())) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "name",
                    PartEntity.getDescription()));
        }
        if (dto.getCategory() == null || dto.getCategory().getId() == null) {
            throw new ValidationException(MessageFormat.format(ENTITY_EMPTY_FIELD, "category",
                    PartEntity.getDescription()));
        }
        partCategoryRepository.findById(dto.getCategory().getId()).orElseThrow(() ->
                new ValidationException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartCategoryEntity.getDescription(),
                        dto.getCategory().getId())));
    }

    @Autowired
    public void setPartRepository(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Autowired
    public void setPartCategoryRepository(PartCategoryRepository partCategoryRepository) {
        this.partCategoryRepository = partCategoryRepository;
    }

    @Autowired
    public void setPartColorRepository(PartColorRepository partColorRepository) {
        this.partColorRepository = partColorRepository;
    }

    @Autowired
    public void setPartNumberDao(PartNumberDao partNumberDao) {
        this.partNumberDao = partNumberDao;
    }

    @Autowired
    public void setPartNumberRepository(PartNumberRepository partNumberRepository) {
        this.partNumberRepository = partNumberRepository;
    }
}
