package ru.shark.home.legomanager.dao.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.services.dto.ListRequest;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.legomanager.dao.dto.SetPartFullDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.entity.SetPartEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.List;

import static ru.shark.home.common.common.ErrorConstants.*;

public class SetPartDaoTest extends DaoServiceTest {
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
    public void getPartsBySetId() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        List<SetPartFullDto> list = setPartDao.getPartsBySetId(setId, null);

        // THEN
        Assertions.assertEquals(2, list.size());
        for (SetPartFullDto dto : list) {
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
    public void getPartsBySetIdWithRequestEmptySearch() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        List<SetPartFullDto> list = setPartDao.getPartsBySetId(setId, new ListRequest());

        // THEN
        Assertions.assertEquals(2, list.size());
        for (SetPartFullDto dto : list) {
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
        ListRequest request = new ListRequest();
        request.setSearch(new Search("555"));

        // WHEN
        List<SetPartFullDto> list = setPartDao.getPartsBySetId(setId, request);

        // THEN
        Assertions.assertEquals(1, list.size());
        for (SetPartFullDto dto : list) {
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
        entity.getPartColor().setId(entityFinder.findPartColorId("55531"));
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
