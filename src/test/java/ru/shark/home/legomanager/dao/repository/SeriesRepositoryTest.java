package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

public class SeriesRepositoryTest extends DaoServiceTest {

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
}
