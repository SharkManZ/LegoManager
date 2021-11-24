package ru.shark.home.legomanager.dao.repository;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Comparator;
import java.util.List;

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

    @Test
    public void getAllCategories() {
        // GIVEN
        Ordering<PartCategoryEntity> ordering = new Ordering<PartCategoryEntity>() {
            @Override
            public int compare(@Nullable PartCategoryEntity partCategoryEntity, @Nullable PartCategoryEntity t1) {
                return Comparator.comparing(PartCategoryEntity::getName)
                        .compare(partCategoryEntity, t1);
            }
        };

        // WHEN
        List<PartCategoryEntity> allSeries = partCategoryRepository.getAllCategories();

        // THEN
        Assertions.assertEquals(2L, allSeries.size());
        Assertions.assertTrue(ordering.isOrdered(allSeries));
    }
}
