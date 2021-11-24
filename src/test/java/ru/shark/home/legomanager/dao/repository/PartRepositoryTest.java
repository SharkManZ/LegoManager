package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartRepositoryTest extends DaoServiceTest {
    @Autowired
    private PartRepository partRepository;

    @BeforeAll
    public void init() {
        loadPartCategories("PartRepositoryTest/partCats.json");
        loadColors("PartRepositoryTest/colors.json");
        loadParts("PartRepositoryTest/parts.json");
    }

    @Test
    public void findSetByNumber() {
        // WHEN
        PartEntity entity = partRepository.findPartByNumber("3010");

        // THEN
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(entity.getNumber(), "3010");
    }

    @Test
    public void getSetCountBySeries() {
        // GIVEN
        Long categoryId = entityFinder.findPartCategoryId("Brick");

        // WHEN
        Long count = partRepository.getPartCountByCategory(categoryId);

        // THEN
        Assertions.assertEquals(2L, count);
    }

    @Test
    public void getCount() {
        // WHEN
        Long count = partRepository.getPartsCount();

        // THEN
        Assertions.assertEquals(2L, count);
    }

    @Test
    public void getPartColorsCountByIds() {
        // GIVEN
        Map<Long, Long> counts = new HashMap<>();
        counts.put(entityFinder.findPartId("3010"), 2L);
        counts.put(entityFinder.findPartId("3001"), 0L);

        // WHEN
        List<Map<String, Long>> partCounts = partRepository.getPartColorsCountByIds(new ArrayList<>(counts.keySet()));

        // THEN
        Assertions.assertEquals(counts.size(), partCounts.size());
        partCounts.forEach(count -> {
            Assertions.assertEquals(counts.get(count.get("id")), count.get("cnt"));
        });
    }
}
