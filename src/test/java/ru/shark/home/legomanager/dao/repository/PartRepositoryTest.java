package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.Arrays;
import java.util.List;

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
    public void findPartByNumber() {
        // WHEN
        PartEntity entity = partRepository.findPartByNumber("3010");

        // THEN
        Assertions.assertNotNull(entity);
        Assertions.assertTrue(entity.getNumbers().stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("3010")));
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
    public void findByCategoryId() {
        // GIVEN
        Long categoryId = entityFinder.findPartCategoryId("Brick");

        // WHEN
        List<PartEntity> list = partRepository.findByCategoryId(categoryId);

        // THEN
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertTrue(list.stream().allMatch(item -> item.getCategory().getId().equals(categoryId)));
    }

    @Test
    public void getPartIdsByNumbers() {
        // GIVEN
        List<Long> ids = Arrays.asList(entityFinder.findPartId("3010"), entityFinder.findPartId("3001"));

        // WHEN
        List<Long> partIdsByNumbers = partRepository.getPartIdsByNumbers(Arrays.asList("3010", "3001"));

        // THEN
        Assertions.assertEquals(2, partIdsByNumbers.size());
        Assertions.assertTrue(partIdsByNumbers.containsAll(ids));
    }
}
