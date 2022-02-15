package ru.shark.home.legomanager.dao.repository;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PartColorRepositoryTest extends DaoServiceTest {
    @Autowired
    private PartColorRepository partColorRepository;

    @BeforeAll
    public void init() {
        loadPartCategories("PartColorRepositoryTest/partCats.json");
        loadColors("PartColorRepositoryTest/colors.json");
        loadParts("PartColorRepositoryTest/parts.json");
    }

    @Test
    public void getPartColorsByPartId() {
        // GIVEN
        Long partId = entityFinder.findPartId("3010");
        // GIVEN
        Ordering<PartColorEntity> ordering = new Ordering<PartColorEntity>() {
            @Override
            public int compare(@Nullable PartColorEntity partColorEntity, @Nullable PartColorEntity t1) {
                return Comparator.comparing(PartColorEntity::getNumber)
                        .compare(partColorEntity, t1);
            }
        };

        // WHEN
        List<PartColorEntity> list = partColorRepository.getPartColorsByPartId(partId);

        // THEN
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(ordering.isOrdered(list));
    }

    @Test
    public void getPartColorsByPartIds() {
        // GIVEN
        List<Long> ids = Arrays.asList(entityFinder.findPartId("3010"), entityFinder.findPartId("3011"));

        // GIVEN
        Ordering<PartColorEntity> ordering = new Ordering<PartColorEntity>() {
            @Override
            public int compare(@Nullable PartColorEntity partColorEntity, @Nullable PartColorEntity t1) {
                int result = partColorEntity.getPart().getId().compareTo(t1.getPart().getId());
                if (result != 0) {
                    return result;
                }
                return Comparator.comparing(PartColorEntity::getNumber)
                        .compare(partColorEntity, t1);
            }
        };

        // WHEN
        List<PartColorEntity> list = partColorRepository.getPartColorsByPartIds(ids);

        // THEN
        Assertions.assertEquals(4, list.size());
        Assertions.assertTrue(ordering.isOrdered(list));
    }

    @Test
    public void getPartColorByNumber() {
        // WHEN
        PartColorEntity byNumber = partColorRepository.getPartColorByNumber("112231");

        // THEN
        Assertions.assertNotNull(byNumber);
        Assertions.assertEquals("112231", byNumber.getNumber());
    }

    @Test
    public void getPartColorByAlternateNumber() {
        // WHEN
        PartColorEntity byNumber = partColorRepository.getPartColorByNumber("898");

        // THEN
        Assertions.assertNotNull(byNumber);
        Assertions.assertEquals("112231", byNumber.getNumber());
        Assertions.assertEquals("898", byNumber.getAlternateNumber());
    }

    @Test
    public void getPartColorByNumberPartNumber() {
        // WHEN
        PartColorEntity byNumber = partColorRepository.getPartColorByNumberPartNumber("112231", "3010");

        // THEN
        Assertions.assertNotNull(byNumber);
        Assertions.assertEquals("112231", byNumber.getNumber());
        Assertions.assertEquals("3010", byNumber.getPart().getNumber());
    }

    @Test
    public void getPartColorByAlternateNumberPartNumber() {
        // WHEN
        PartColorEntity byNumber = partColorRepository.getPartColorByNumberPartNumber("898", "3010");

        // THEN
        Assertions.assertNotNull(byNumber);
        Assertions.assertEquals("112231", byNumber.getNumber());
        Assertions.assertEquals("898", byNumber.getAlternateNumber());
        Assertions.assertEquals("3010", byNumber.getPart().getNumber());
    }

    @Test
    public void getPartColorByPartAndColor() {
        // GIVEN
        Long partId = entityFinder.findPartId("3010");
        Long colorId = entityFinder.findColorId("Black");

        // WHEN
        PartColorEntity entity = partColorRepository.getPartColorByPartAndColor(partId, colorId);

        // THEN
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(partId, entity.getPart().getId());
        Assertions.assertEquals(colorId, entity.getColor().getId());
    }
}
