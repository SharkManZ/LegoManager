package ru.shark.home.legomanager.dao.repository;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

public class SeriesRepositoryTest extends DbTest {

    @Autowired
    private SeriesRepository seriesRepository;

    @BeforeAll
    public void init() {
        loadSeries("SeriesRepositoryTest/series.json");
        loadSets("SeriesRepositoryTest/sets.json");
    }

    @Test
    public void findSeriesByName() {
        // GIVEN
        SeriesEntity entity = seriesRepository.findSeriesByName("Technic");

        // WHEN
        SeriesEntity foundEntity = seriesRepository.findSeriesByName("Technic");

        // TEN
        Assertions.assertTrue(new ReflectionEquals(entity).matches(foundEntity));
    }

    @Test
    public void getCount() {
        // WHEN
        Long count = seriesRepository.getCount();

        // THEN
        Assertions.assertEquals(2L, count);
    }

    @Test
    public void getSeriesSetsCountByIds() {
        // GIVEN
        List<Long> ids = Arrays.asList(entityFinder.findSeriesId("Technic"),
                entityFinder.findSeriesId("City"));
        Map<Long, Long> counts = new HashMap<>();
        counts.put(entityFinder.findSeriesId("Technic"), 2L);
        counts.put(entityFinder.findSeriesId("City"), 1L);

        // WHEN
        List<Map<String, Object>> list = seriesRepository.getSeriesSetsCountByIds(ids);

        // THEN
        Assertions.assertFalse(isEmpty(list));
        Assertions.assertEquals(list.size(), ids.size());
        list.forEach(item -> {
            Assertions.assertEquals(counts.get(item.get("id")), (Long) item.get("cnt"));
        });
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
        List<SeriesEntity> allSeries = seriesRepository.getAllSeries();

        // THEN
        Assertions.assertEquals(2L, allSeries.size());
        Assertions.assertTrue(ordering.isOrdered(allSeries));
    }
}
