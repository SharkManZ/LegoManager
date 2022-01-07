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
import ru.shark.home.legomanager.dao.dto.SeriesFullDto;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.legomanager.common.ErrorConstants.SERIES_DELETE_WITH_SETS;

public class SeriesDaoTest extends DaoServiceTest {

    @Autowired
    private SeriesDao seriesDao;

    @BeforeAll
    public void init() {
        loadSeries("SeriesDaoTest/series.json");
        loadSets("SeriesDaoTest/sets.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<SeriesFullDto> ordering = new Ordering<SeriesFullDto>() {
            @Override
            public int compare(@Nullable SeriesFullDto seriesEntity, @Nullable SeriesFullDto t1) {
                return Comparator.comparing(SeriesFullDto::getName)
                        .compare(seriesEntity, t1);
            }
        };

        // WHEN
        PageableList<SeriesFullDto> list = seriesDao.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 4, 4L);
        assertTrue(ordering.isOrdered(list.getData()));
        boolean setsCountChecked = false;
        for (SeriesFullDto item : list.getData()) {
            if (item.getName().equalsIgnoreCase("technic")) {
                Assertions.assertEquals(2, item.getSetsCount());
                setsCountChecked = true;
            }
            Assertions.assertEquals(item.getImgName(), item.getName()
                    .toLowerCase()
                    .replaceAll(" ", "_")
                    .replaceAll("-", "_"));
        }

        Assertions.assertTrue(setsCountChecked);
    }

    @Test
    public void getWithPaginationWithSearch() {
        // GIVEN
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch(new RequestSearch("Tech", false));

        // WHEN
        PageableList<SeriesFullDto> list = seriesDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingDtoList(list, 1, 1L);
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        SeriesEntity entity = prepareEntity();

        // WHEN
        SeriesEntity saved = seriesDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        SeriesEntity entity = prepareEntity();
        entity.setId(entityFinder.findSeriesId("Technic"));

        // WHEN
        SeriesEntity saved = seriesDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
    }

    @Test()
    public void saveWithAlreadyExistsByName() {
        // GIVEN
        SeriesEntity entity = prepareEntity();
        entity.setName("Technic");
        String expectedMsg = MessageFormat.format(ENTITY_ALREADY_EXISTS,
                SeriesEntity.getDescription(), entity.getName());

        // WHEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> seriesDao.save(entity));

        // THEN
        Assertions.assertEquals(expectedMsg, exception.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findSeriesId("Creator 3-in-1");

        // WHEN
        seriesDao.deleteById(id);

        // THEN
        assertTrue(isDeleted(id, SeriesEntity.class));
    }

    @Test
    public void deleteByIdWithSets() {
        // GIVEN
        Long id = entityFinder.findSeriesId("Technic");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> seriesDao.deleteById(id));

        // THEN
        Assertions.assertEquals(SERIES_DELETE_WITH_SETS, validationException.getMessage());
    }

    @Test
    public void getAllSeries() {
        // GIVEN
        Ordering<SeriesEntity> ordering = new Ordering<SeriesEntity>() {
            @Override
            public int compare(@Nullable SeriesEntity seriesEntity, @Nullable SeriesEntity t1) {
                return Comparator.comparing(SeriesEntity::getName)
                        .compare(seriesEntity, t1);
            }
        };

        // WHEN
        List<SeriesEntity> allSeries = seriesDao.getAllSeries();

        // THEN
        Assertions.assertEquals(4L, allSeries.size());
        Assertions.assertTrue(ordering.isOrdered(allSeries));
    }

    private SeriesEntity prepareEntity() {
        SeriesEntity entity = new SeriesEntity();
        entity.setName("someName");

        return entity;
    }
}
