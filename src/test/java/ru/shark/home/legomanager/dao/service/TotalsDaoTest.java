package ru.shark.home.legomanager.dao.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.TotalDto;
import ru.shark.home.legomanager.util.DaoServiceTest;

public class TotalsDaoTest extends DaoServiceTest {
    @Autowired
    private TotalsDao totalsDao;

    @BeforeAll
    public void init() {
        loadSeries("TotalsDaoTest/series.json");
        loadSets("TotalsDaoTest/sets.json");
    }

    @Test
    public void getSeriesTotal() {
        // WHEN
        TotalDto seriesTotal = totalsDao.getSeriesTotal();

        // THEN
        Assertions.assertNotNull(seriesTotal);
        Assertions.assertEquals(3L, seriesTotal.getTotal());
        Assertions.assertEquals(0L, seriesTotal.getInStock());
    }

    @Test
    public void getSetsTotal() {
        // WHEN
        TotalDto setsTotal = totalsDao.getSetsTotal();

        // THEN
        Assertions.assertNotNull(setsTotal);
        Assertions.assertEquals(3L, setsTotal.getTotal());
        Assertions.assertEquals(0L, setsTotal.getInStock());
    }
}
