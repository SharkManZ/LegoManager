package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

public class SetRepositoryTest extends DaoServiceTest {
    @Autowired
    private SetRepository setRepository;

    @BeforeAll
    public void init() {
        loadSeries("SetRepositoryTest/series.json");
        loadSets("SetRepositoryTest/sets.json");
    }

    @Test
    public void findSetByNumber() {
        // WHEN
        SetEntity entity = setRepository.findSetByNumber("42082");

        // THEN
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(entity.getNumber(), "42082");
    }

    @Test
    public void getSetCountBySeries() {
        // GIVEN
        Long seriesId = entityFinder.findSeriesId("Technic");

        // WHEN
        Long count = setRepository.getSetCountBySeries(seriesId);

        // THEN
        Assertions.assertEquals(2L, count);
    }

    @Test
    public void getCount() {
        // WHEN
        Long count = setRepository.getCount();

        // THEN
        Assertions.assertEquals(3L, count);
    }
}
