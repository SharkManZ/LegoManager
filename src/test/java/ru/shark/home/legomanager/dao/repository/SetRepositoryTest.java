package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetRepositoryTest extends DaoServiceTest {
    @Autowired
    private SetRepository setRepository;

    @BeforeAll
    public void init() {
        loadSeries("SetRepositoryTest/series.json");
        loadColors("SetRepositoryTest/colors.json");
        loadPartCategories("SetRepositoryTest/partCats.json");
        loadParts("SetRepositoryTest/parts.json");
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

    @Test
    public void getSetsAdditionalData() {
        // GIVEN
        Long setWithParts = entityFinder.findSetId("42082");
        List<Long> ids = Arrays.asList(setWithParts, entityFinder.findSetId("42100"));

        // WHEN
        List<Map<String, Object>> list = setRepository.getSetsAdditionalData(ids);

        // THEN
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(list.stream().anyMatch(item -> item.get("id").equals(setWithParts) && (Long) item.get("partsCount") == 13L));
    }

    @Test
    public void findBySeriesId() {
        // GIVEN
        Long seriesId = entityFinder.findSeriesId("Technic");

        // WHEN
        List<SetEntity> list = setRepository.findBySeriesId(seriesId);

        // THEN
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(list.stream().allMatch(item -> item.getSeries().getId().equals(seriesId)));
    }

    @Test
    public void getSetSummary() {
        // GIVEN
        SetEntity set = entityFinder.findSet("42082");

        // WHEN
        Map<String, Object> map = setRepository.getSetSummary(set.getId());

        // THEN
        Assertions.assertEquals(5, map.size());
        Assertions.assertEquals(set.getNumber(), map.get("number"));
        Assertions.assertEquals(set.getName(), map.get("name"));
        Assertions.assertEquals(set.getYear(), map.get("year"));
        Assertions.assertEquals(13L, map.get("partsCount"));
        Assertions.assertEquals(2L, map.get("uniquePartsCount"));
    }
}
