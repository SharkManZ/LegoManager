package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.common.RequestSearch;
import ru.shark.home.legomanager.dao.dto.SetFullDto;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.shark.home.common.common.ErrorConstants.*;

public class SetDaoTest extends DaoServiceTest {

    @Autowired
    private SetDao setDao;

    @BeforeAll
    public void init() {
        loadSeries("SetDaoTest/series.json");
        loadColors("SetDaoTest/colors.json");
        loadPartCategories("SetDaoTest/partCats.json");
        loadParts("SetDaoTest/parts.json");
        loadSets("SetDaoTest/sets.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<SetFullDto> ordering = new Ordering<SetFullDto>() {
            @Override
            public int compare(@Nullable SetFullDto setDto, @Nullable SetFullDto t1) {
                return Comparator.comparing(SetFullDto::getNumber)
                        .compare(setDto, t1);
            }
        };

        // WHEN
        PageableList<SetFullDto> list = setDao.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 3, 3L);
        Assertions.assertTrue(list.getData().stream().anyMatch(item -> item.getPartsCount() > 0));
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void getWithPaginationWithSearch() {
        // GIVEN
        Ordering<SetFullDto> ordering = new Ordering<SetFullDto>() {
            @Override
            public int compare(@Nullable SetFullDto setDto, @Nullable SetFullDto t1) {
                return Comparator.comparing(SetFullDto::getName)
                        .compare(setDto, t1);
            }
        };
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch(new RequestSearch("421", false));

        // WHEN
        PageableList<SetFullDto> list = setDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        SetEntity entity = prepareEntity();

        // WHEN
        SetEntity saved = setDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
        Assertions.assertEquals(entity.getNumber(), saved.getNumber());
        Assertions.assertEquals(entity.getYear(), saved.getYear());
        Assertions.assertEquals(entity.getSeries().getId(), saved.getSeries().getId());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        SetEntity entity = prepareEntity();
        entity.setId(entityFinder.findSetId("42082"));

        // WHEN
        SetEntity saved = setDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
        Assertions.assertEquals(entity.getNumber(), saved.getNumber());
        Assertions.assertEquals(entity.getYear(), saved.getYear());
        Assertions.assertEquals(entity.getSeries().getId(), saved.getSeries().getId());
    }

    @Test
    public void saveWithCreateAndExists() {
        // GIVEN
        SetEntity entity = prepareEntity();
        entity.setNumber("42082");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> setDao.save(entity));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, SetEntity.getDescription(),
                entity.getNumber()), validationException.getMessage());
    }

    @Test
    public void saveWithUpdateAndExists() {
        // GIVEN
        SetEntity entity = prepareEntity();
        entity.setId(entityFinder.findSetId("42082"));
        entity.setNumber("42100");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> setDao.save(entity));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, SetEntity.getDescription(),
                entity.getNumber()), validationException.getMessage());
    }

    @Test
    public void saveWithValidation() {
        // GIVEN
        SetEntity entityNoName = prepareEntity();
        entityNoName.setName(null);
        SetEntity entityNoNumber = prepareEntity();
        entityNoNumber.setNumber(null);
        SetEntity entityNoYear = prepareEntity();
        entityNoYear.setYear(null);
        SetEntity entityNoSeries = prepareEntity();
        entityNoSeries.setSeries(null);
        SetEntity entityNoSeriesId = prepareEntity();
        entityNoSeriesId.getSeries().setId(null);
        SetEntity entitySeriesNotFound = prepareEntity();
        entitySeriesNotFound.getSeries().setId(999L);

        // WHEN
        ValidationException noName = assertThrows(ValidationException.class, () -> setDao.save(entityNoName));
        ValidationException noNumber = assertThrows(ValidationException.class, () -> setDao.save(entityNoNumber));
        ValidationException noYear = assertThrows(ValidationException.class, () -> setDao.save(entityNoYear));
        ValidationException noSeries = assertThrows(ValidationException.class, () -> setDao.save(entityNoSeries));
        ValidationException noSeriesId = assertThrows(ValidationException.class, () -> setDao.save(entityNoSeriesId));
        ValidationException seriesNotFound = assertThrows(ValidationException.class, () -> setDao.save(entitySeriesNotFound));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "name",
                SetEntity.getDescription()), noName.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                SetEntity.getDescription()), noNumber.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "year",
                SetEntity.getDescription()), noYear.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "series",
                SetEntity.getDescription()), noSeries.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "series",
                SetEntity.getDescription()), noSeriesId.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, SeriesEntity.getDescription(),
                entitySeriesNotFound.getSeries().getId()), seriesNotFound.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long setId = entityFinder.findSetId("42100");

        // WHEN
        setDao.deleteById(setId);

        // THEN
        isDeleted(setId, SetEntity.class);
    }

    private SetEntity prepareEntity() {
        SetEntity entity = new SetEntity();
        entity.setSeries(new SeriesEntity());
        entity.getSeries().setId(entityFinder.findSeriesId("technic"));
        entity.setName("newSet");
        entity.setNumber("1012112");
        entity.setYear(2020);
        return entity;
    }
}
