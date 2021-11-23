package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

public class PartCategoryRepositoryTest extends DaoServiceTest {
    @Autowired
    private PartCategoryRepository partCategoryRepository;

    @BeforeAll
    public void init() {
        loadPartCategories("PartCategoryRepositoryTest/partCats.json");
    }

    @Test
    public void findColorByName() {
        // GIVEN
        PartCategoryEntity expected = entityFinder.findPartCategory("Tile");

        // WHEN
        PartCategoryEntity entity = partCategoryRepository.findPartCategoryByName("Tile");

        // THEN
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(expected.getId(), entity.getId());
    }
}
