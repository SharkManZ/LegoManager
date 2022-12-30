package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.common.RequestSearch;
import ru.shark.home.legomanager.dao.dto.PartCategoryDto;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.dao.dto.PartFullDto;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.util.DbTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.shark.home.common.common.ErrorConstants.*;
import static ru.shark.home.legomanager.common.ErrorConstants.PART_DELETE_WIT_COLORS;

public class PartDaoTest extends DbTest {

    @Autowired
    private PartDao partDao;

    @BeforeAll
    public void init() {
        loadPartCategories("PartDaoTest/partCats.json");
        loadColors("PartDaoTest/colors.json");
        loadParts("PartDaoTest/parts.json");
    }

    @Test
    public void getWithPagination() {

        // GIVEN
        Ordering<PartFullDto> ordering = new Ordering<PartFullDto>() {
            @Override
            public int compare(@Nullable PartFullDto partDto, @Nullable PartFullDto t1) {
                return Comparator.comparing(PartFullDto::getNumber)
                        .compare(partDto, t1);
            }
        };
        Map<Long, Pair<Integer, String>> counts = new HashMap<>();
        counts.put(entityFinder.findPartId("3010"), Pair.of(2, "112231"));
        counts.put(entityFinder.findPartId("3001"), Pair.of(0, null));


        // WHEN
        PageableList<PartFullDto> list = partDao.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 2, 2L);
        assertTrue(ordering.isOrdered(list.getData()));
        list.getData().forEach(item -> {
            Assertions.assertEquals(counts.get(item.getId()).getLeft(), item.getColorsCount());
            Assertions.assertEquals(counts.get(item.getId()).getRight(), item.getMinColorNumber());
        });
        Assertions.assertTrue(list.getData().stream().anyMatch(item -> item.getColors().size() > 0));
        Assertions.assertEquals(list.getData()
                .stream()
                .flatMap(item -> item.getColors().stream())
                .filter(item -> !isBlank(item.getName()) && !isBlank(item.getHexColor()))
                .count(), 2);
    }

    @Test
    public void getWithPaginationWithSearch() {
        // GIVEN
        Ordering<PartFullDto> ordering = new Ordering<PartFullDto>() {
            @Override
            public int compare(@Nullable PartFullDto partDto, @Nullable PartFullDto t1) {
                return Comparator.comparing(PartFullDto::getName)
                        .compare(partDto, t1);
            }
        };
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch(new RequestSearch("100", false));

        // WHEN
        PageableList<PartFullDto> list = partDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        PartDto dto = prepareDto();

        // WHEN
        PartDto saved = partDao.savePart(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(dto.getName(), saved.getName());
        Assertions.assertEquals(dto.getNumber(), saved.getNumber());
        Assertions.assertEquals(dto.getCategory().getId(), saved.getCategory().getId());
        Assertions.assertEquals(dto.getAlternateNumber(), saved.getAlternateNumber());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        PartDto dto = prepareDto();
        dto.setId(entityFinder.findPartId("3010"));

        // WHEN
        PartDto saved = partDao.savePart(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(dto.getId(), saved.getId());
        Assertions.assertEquals(dto.getName(), saved.getName());
        Assertions.assertEquals(dto.getNumber(), saved.getNumber());
        Assertions.assertEquals(dto.getCategory().getId(), saved.getCategory().getId());
        Assertions.assertEquals(dto.getAlternateNumber(), saved.getAlternateNumber());
    }

    @Test
    public void saveWithCreateAndExists() {
        // GIVEN
        PartDto dto = prepareDto();
        dto.setNumber("3010");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> partDao.savePart(dto));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartEntity.getDescription(),
                Arrays.asList(dto.getNumber(), dto.getAlternateNumber())), validationException.getMessage());
    }

    @Test
    public void saveWithUpdateAndExists() {
        // GIVEN
        PartDto dto = prepareDto();
        dto.setId(entityFinder.findPartId("3010"));
        dto.setNumber("3001");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> partDao.savePart(dto));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartEntity.getDescription(),
                Arrays.asList(dto.getNumber(), dto.getAlternateNumber())), validationException.getMessage());
    }

    @Test
    public void saveWithValidation() {
        // GIVEN
        PartDto dtoNoName = prepareDto();
        dtoNoName.setName(null);
        PartDto dtoNoNumber = prepareDto();
        dtoNoNumber.setNumber(null);
        PartDto dtoNoCategory = prepareDto();
        dtoNoCategory.setCategory(null);
        PartDto dtoNoCategoryId = prepareDto();
        dtoNoCategoryId.getCategory().setId(null);
        PartDto dtoCategoryNotFound = prepareDto();
        dtoCategoryNotFound.getCategory().setId(999L);

        // WHEN
        ValidationException noName = assertThrows(ValidationException.class, () -> partDao.savePart(dtoNoName));
        ValidationException noNumber = assertThrows(ValidationException.class, () -> partDao.savePart(dtoNoNumber));
        ValidationException noCategory = assertThrows(ValidationException.class, () -> partDao.savePart(dtoNoCategory));
        ValidationException noCategoryId = assertThrows(ValidationException.class, () -> partDao.savePart(dtoNoCategoryId));
        ValidationException categoryNotFound = assertThrows(ValidationException.class, () -> partDao.savePart(dtoCategoryNotFound));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "name",
                PartEntity.getDescription()), noName.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                PartEntity.getDescription()), noNumber.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "category",
                PartEntity.getDescription()), noCategory.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "category",
                PartEntity.getDescription()), noCategoryId.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartCategoryEntity.getDescription(),
                dtoCategoryNotFound.getCategory().getId()), categoryNotFound.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long setId = entityFinder.findPartId("3001");

        // WHEN
        partDao.deleteById(setId);

        // THEN
        isDeleted(setId, SetEntity.class);
    }

    @Test
    public void deleteByIdWithColors() {
        // GIVEN
        Long setId = entityFinder.findPartId("3010");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> partDao.deleteById(setId));

        // THEN
        Assertions.assertEquals(PART_DELETE_WIT_COLORS, validationException.getMessage());
    }

    private PartDto prepareDto() {
        PartDto dto = new PartDto();
        dto.setCategory(new PartCategoryDto(entityFinder.findPartCategoryId("brick"), null));
        dto.setName("plate 1x10");
        dto.setNumber("4488");
        dto.setAlternateNumber("8844");
        return dto;
    }
}
