package ru.shark.home.legomanager.dao.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartRepositoryTest extends DbTest {
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
        Map<Long, Pair<Long, String>> counts = new HashMap<>();
        counts.put(entityFinder.findPartId("3010"), Pair.of(2L, "112231"));
        counts.put(entityFinder.findPartId("3001"), Pair.of(0L, null));

        // WHEN
        List<Map<String, Object>> partCounts = partRepository.getPartAdditionalDataByIds(new ArrayList<>(counts.keySet()));

        // THEN
        Assertions.assertEquals(counts.size(), partCounts.size());
        partCounts.forEach(count -> {
            Assertions.assertEquals(counts.get(count.get("id")).getLeft(), count.get("cnt"));
            Assertions.assertEquals(counts.get(count.get("id")).getRight(), count.get("minColorNumber"));
        });
    }

    @Test
    public void findByCategoryId() {
        // GIVEN
        Long categoryId = entityFinder.findPartCategoryId("Brick");

        // WHEN
        List<PartEntity> list = partRepository.findByCategoryId(categoryId);

        // THEN
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertTrue(list.stream().allMatch(item -> item.getCategory().getId().equals(categoryId)));
    }
}
