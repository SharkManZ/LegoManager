package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

public class SeriesRepositoryTest extends DaoServiceTest {

    @Autowired
    private SeriesRepository seriesRepository;

    @BeforeAll
    public void init() {
        loadSeries("SeriesRepositoryTest/series.json");
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
        Assertions.assertEquals(1L, count);
    }
}
