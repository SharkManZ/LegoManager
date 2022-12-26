package ru.shark.home.legomanager.dao.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.TotalDto;
import ru.shark.home.legomanager.services.dto.TotalsRequestDto;
import ru.shark.home.legomanager.util.DbTest;

public class TotalsDaoTest extends DbTest {
    @Autowired
    private TotalsDao totalsDao;

    @BeforeAll
    public void init() {
        loadSeries("TotalsDaoTest/series.json");
        loadColors("TotalsDaoTest/colors.json");
        loadPartCategories("TotalsDaoTest/partCats.json");
        loadParts("TotalsDaoTest/parts.json");
        loadSets("TotalsDaoTest/sets.json");
        loadUsers("TotalsDaoTest/users.json");
    }

    @Test
    public void getSeriesTotal() {
        // WHEN
        TotalDto seriesTotal = totalsDao.getSeriesTotal(null);

        // THEN
        Assertions.assertNotNull(seriesTotal);
        Assertions.assertEquals(3L, seriesTotal.getTotal());
        Assertions.assertEquals(0L, seriesTotal.getInStock());
    }

    @Test
    public void getSeriesTotalWithUserId() {
        // GIVEN
        TotalsRequestDto requestDto = new TotalsRequestDto(entityFinder.findUserId("Максим"));

        // WHEN
        TotalDto seriesTotal = totalsDao.getSeriesTotal(requestDto);

        // THEN
        Assertions.assertNotNull(seriesTotal);
        Assertions.assertEquals(3L, seriesTotal.getTotal());
        Assertions.assertEquals(1L, seriesTotal.getInStock());
    }

    @Test
    public void getSetsTotal() {
        // WHEN
        TotalDto setsTotal = totalsDao.getSetsTotal(null);

        // THEN
        Assertions.assertNotNull(setsTotal);
        Assertions.assertEquals(3L, setsTotal.getTotal());
        Assertions.assertEquals(0L, setsTotal.getInStock());
    }

    @Test
    public void getSetsTotalWithUserId() {
        // GIVEN
        TotalsRequestDto requestDto = new TotalsRequestDto(entityFinder.findUserId("Максим"));

        // WHEN
        TotalDto setsTotal = totalsDao.getSetsTotal(requestDto);

        // THEN
        Assertions.assertNotNull(setsTotal);
        Assertions.assertEquals(3L, setsTotal.getTotal());
        Assertions.assertEquals(3L, setsTotal.getInStock());
    }

    @Test
    public void getPartsTotal() {
        // WHEN
        TotalDto setsTotal = totalsDao.getPartsTotal(null);

        // THEN
        Assertions.assertNotNull(setsTotal);
        Assertions.assertEquals(2L, setsTotal.getTotal());
        Assertions.assertEquals(0L, setsTotal.getInStock());
    }

    @Test
    public void getPartsTotalWithUserId() {
        // GIVEN
        TotalsRequestDto requestDto = new TotalsRequestDto(entityFinder.findUserId("Максим"));

        // WHEN
        TotalDto setsTotal = totalsDao.getPartsTotal(requestDto);

        // THEN
        Assertions.assertNotNull(setsTotal);
        Assertions.assertEquals(2L, setsTotal.getTotal());
        Assertions.assertEquals(2L, setsTotal.getInStock());
    }

    @Test
    public void getPartColorsTotal() {
        // WHEN
        TotalDto setPartsTotal = totalsDao.getPartColorsTotal(null);

        // THEN
        Assertions.assertNotNull(setPartsTotal);
        Assertions.assertEquals(13L, setPartsTotal.getTotal());
        Assertions.assertEquals(0L, setPartsTotal.getInStock());
    }

    @Test
    public void getPartColorsTotalWithUserId() {
        // GIVEN
        TotalsRequestDto requestDto = new TotalsRequestDto(entityFinder.findUserId("Максим"));

        // WHEN
        TotalDto setPartsTotal = totalsDao.getPartColorsTotal(requestDto);

        // THEN
        Assertions.assertNotNull(setPartsTotal);
        Assertions.assertEquals(13L, setPartsTotal.getTotal());
        Assertions.assertEquals(19L, setPartsTotal.getInStock());
    }

}
