package ru.shark.home.legomanager.dao.repository;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.Comparator;
import java.util.List;

public class ColorRepositoryTest extends DbTest {
    @Autowired
    private ColorRepository colorRepository;

    @BeforeAll
    public void init() {
        loadColors("ColorRepositoryTest/colors.json");
        loadPartCategories("ColorRepositoryTest/partCats.json");
        loadParts("ColorRepositoryTest/parts.json");
        loadSeries("ColorRepositoryTest/series.json");
        loadSets("ColorRepositoryTest/sets.json");
    }

    @Test
    public void findColorByName() {
        // GIVEN
        ColorEntity expected = entityFinder.findColor("Black");

        // WHEN
        ColorEntity black = colorRepository.findColorByName("Black");

        // THEN
        Assertions.assertNotNull(black);
        Assertions.assertEquals(expected.getId(), black.getId());
    }

    @Test
    public void getAllColors() {
        // GIVEN
        Ordering<ColorEntity> ordering = new Ordering<ColorEntity>() {
            @Override
            public int compare(@Nullable ColorEntity colorEntity, @Nullable ColorEntity t1) {
                return Comparator.comparing(ColorEntity::getName)
                        .compare(colorEntity, t1);
            }
        };

        // WHEN
        List<ColorEntity> colors = colorRepository.getAllColors();

        // THEN
        Assertions.assertEquals(2, colors.size());
        Assertions.assertTrue(ordering.isOrdered(colors));
    }

    @Test
    public void getColorsBySetId() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        List<ColorEntity> colors = colorRepository.getColorsBySetId(setId);

        // THEN
        Assertions.assertEquals(2, colors.size());
    }
}
