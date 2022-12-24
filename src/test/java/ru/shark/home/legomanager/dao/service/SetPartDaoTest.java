package ru.shark.home.legomanager.dao.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.*;
import ru.shark.home.common.enums.FieldType;
import ru.shark.home.legomanager.dao.dto.SetPartFullDto;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.entity.SetPartEntity;
import ru.shark.home.legomanager.util.DbTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Arrays;

import static ru.shark.home.common.common.ErrorConstants.*;

public class SetPartDaoTest extends DbTest {
    @Autowired
    private SetPartDao setPartDao;

    @BeforeAll
    public void init() {
        loadSeries("SetPartDaoTest/series.json");
        loadColors("SetPartDaoTest/colors.json");
        loadPartCategories("SetPartDaoTest/partCats.json");
        loadParts("SetPartDaoTest/parts.json");
        loadSets("SetPartDaoTest/sets.json");
    }

    @Test
    public void getPartsBySetIdWithColorFilter() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");
        ColorEntity color = entityFinder.findColor("Black");
        RequestCriteria request = new RequestCriteria(0, 10);
        request.setFilters(Arrays.asList(new RequestFilter("colorId", FieldType.INTEGER, "=", color.getId().toString())));

        // WHEN
        PageableList<SetPartFullDto> list = setPartDao.getPartsBySetId(setId, request);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        for (SetPartFullDto dto : list.getData()) {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getSetId());
            Assertions.assertNotNull(dto.getPartColorId());
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getColorNumber());
            Assertions.assertNotNull(dto.getHexColor());
            Assertions.assertEquals(color.getHexColor(), dto.getHexColor());
            Assertions.assertNotNull(dto.getCount());
            Assertions.assertNotNull(dto.getPartName());
        }
    }

    @Test
    public void getPartsBySetIdWithCategoryFilter() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");
        Long catId = entityFinder.findPartCategoryId("Tile");
        Long partColorId = entityFinder.findPartColorId("55521");
        RequestCriteria request = new RequestCriteria(0, 10);
        request.setFilters(Arrays.asList(new RequestFilter("categoryId", FieldType.INTEGER, "=", catId.toString())));

        // WHEN
        PageableList<SetPartFullDto> list = setPartDao.getPartsBySetId(setId, request);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        for (SetPartFullDto dto : list.getData()) {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getSetId());
            Assertions.assertNotNull(dto.getPartColorId());
            Assertions.assertEquals(partColorId, dto.getPartColorId());
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getColorNumber());
            Assertions.assertNotNull(dto.getHexColor());
            Assertions.assertNotNull(dto.getCount());
            Assertions.assertNotNull(dto.getPartName());
        }
    }

    @Test
    public void getPartsBySetIdWithRequestEmptySearch() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        PageableList<SetPartFullDto> list = setPartDao.getPartsBySetId(setId, new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 2, 2L);
        for (SetPartFullDto dto : list.getData()) {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getSetId());
            Assertions.assertNotNull(dto.getPartColorId());
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getColorNumber());
            Assertions.assertNotNull(dto.getHexColor());
            Assertions.assertNotNull(dto.getCount());
            Assertions.assertNotNull(dto.getPartName());
        }
    }

    @Test
    public void getPartsBySetIdWithSort() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSorts(Arrays.asList(new RequestSort("colorNumber", "desc")));

        // WHEN
        PageableList<SetPartFullDto> list = setPartDao.getPartsBySetId(setId, requestCriteria);

        // THEN
        checkPagingDtoList(list, 2, 2L);
        for (SetPartFullDto dto : list.getData()) {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getSetId());
            Assertions.assertNotNull(dto.getPartColorId());
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getColorNumber());
            Assertions.assertNotNull(dto.getHexColor());
            Assertions.assertNotNull(dto.getCount());
            Assertions.assertNotNull(dto.getPartName());
        }
    }

    @Test
    public void getPartsBySetIdWithSearch() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");
        RequestCriteria request = new RequestCriteria(0, 10);
        request.setSearch(new RequestSearch("555", false));

        // WHEN
        PageableList<SetPartFullDto> list = setPartDao.getPartsBySetId(setId, request);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        for (SetPartFullDto dto : list.getData()) {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getSetId());
            Assertions.assertNotNull(dto.getPartColorId());
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getColorNumber());
            Assertions.assertNotNull(dto.getHexColor());
            Assertions.assertNotNull(dto.getCount());
            Assertions.assertNotNull(dto.getPartName());
        }
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        SetPartEntity entity = prepareEntity();

        // WHEN
        SetPartEntity saved = setPartDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getCount(), saved.getCount());
        Assertions.assertEquals(entity.getSet().getId(), saved.getSet().getId());
        Assertions.assertEquals(entity.getPartColor().getId(), saved.getPartColor().getId());
    }

    @Test
    public void saveWithCreateAndExistsBySetAndPartColor() {
        // GIVEN
        SetPartEntity entity = prepareEntity();
        entity.getSet().setId(entityFinder.findSetId("42082"));
        entity.getPartColor().setId(entityFinder.findPartColorId("112231"));

        // WHEN
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(entity));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, SetPartEntity.getDescription(),
                entity.getSet().getId() + " " + entity.getPartColor().getId()), validationException.getMessage());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        SetPartEntity entity = prepareEntity();
        entity.getSet().setId(entityFinder.findSetId("42082"));
        entity.getPartColor().setId(entityFinder.findPartColorId("112231"));
        entity.setId(entityFinder.findSetPartId(entity.getSet().getId(), entity.getPartColor().getId()));

        // WHEN
        SetPartEntity saved = setPartDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getCount(), saved.getCount());
        Assertions.assertEquals(entity.getSet().getId(), saved.getSet().getId());
        Assertions.assertEquals(entity.getPartColor().getId(), saved.getPartColor().getId());
    }

    @Test
    public void saveWithUpdateAndExistsBySetAndPartColor() {
        // GIVEN
        SetPartEntity entity = prepareEntity();
        entity.getSet().setId(entityFinder.findSetId("42082"));
        entity.getPartColor().setId(entityFinder.findPartColorId("55521"));
        entity.setId(entityFinder.findSetPartId(entity.getSet().getId(), entityFinder.findPartColorId("112231")));

        // WHEN
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(entity));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, SetPartEntity.getDescription(),
                entity.getSet().getId() + " " + entity.getPartColor().getId()), validationException.getMessage());
    }

    @Test
    public void saveWithValidate() {
        // GIVEN
        SetPartEntity noCount = prepareEntity();
        noCount.setCount(null);
        SetPartEntity noSet = prepareEntity();
        noSet.setSet(null);
        SetPartEntity noSetId = prepareEntity();
        noSetId.getSet().setId(null);
        SetPartEntity setNotFound = prepareEntity();
        setNotFound.getSet().setId(999L);
        SetPartEntity noPartColor = prepareEntity();
        noPartColor.setPartColor(null);
        SetPartEntity noPartColorId = prepareEntity();
        noPartColorId.getPartColor().setId(null);
        SetPartEntity partColorNotFound = prepareEntity();
        partColorNotFound.getPartColor().setId(999L);
        SetPartEntity invalidCount = prepareEntity();
        invalidCount.setCount(0);

        // WHEN
        ValidationException noCountException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(noCount));
        ValidationException noSetException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(noSet));
        ValidationException noSetIdException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(noSetId));
        ValidationException setNotFoundException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(setNotFound));
        ValidationException noPartColorException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(noPartColor));
        ValidationException noPartColorIdException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(noPartColorId));
        ValidationException partColorNotFoundException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(partColorNotFound));
        ValidationException invalidCountException = Assertions.assertThrows(ValidationException.class, () -> setPartDao.save(invalidCount));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "count",
                SetPartEntity.getDescription()), noCountException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "set",
                SetPartEntity.getDescription()), noSetException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "set",
                SetPartEntity.getDescription()), noSetIdException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, SetEntity.getDescription(),
                setNotFound.getSet().getId()), setNotFoundException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "partColor",
                SetPartEntity.getDescription()), noPartColorException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "partColor",
                SetPartEntity.getDescription()), noPartColorIdException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartColorEntity.getDescription(),
                setNotFound.getSet().getId()), partColorNotFoundException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_FIELD_VALUE_LOWER, "count", 0),
                invalidCountException.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long setPartId = entityFinder.findSetPartId(entityFinder.findSetId("42082"),
                entityFinder.findPartColorId("112231"));

        // WHEN
        setPartDao.deleteById(setPartId);

        // THEN
        Assertions.assertTrue(isDeleted(setPartId, SetPartEntity.class));
    }

    private SetPartEntity prepareEntity() {
        SetPartEntity entity = new SetPartEntity();
        entity.setCount(10);
        entity.setSet(new SetEntity());
        entity.getSet().setId(entityFinder.findSetId("42100"));
        entity.setPartColor(new PartColorEntity());
        entity.getPartColor().setId(entityFinder.findPartColorId("112231"));

        return entity;
    }
}
