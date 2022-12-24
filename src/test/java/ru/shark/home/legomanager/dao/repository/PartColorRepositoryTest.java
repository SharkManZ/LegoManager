package ru.shark.home.legomanager.dao.repository;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorNumberEntity;
import ru.shark.home.legomanager.util.DbTest;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PartColorRepositoryTest extends DbTest {
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
                String color1 = partColorEntity.getNumbers().stream().filter(PartColorNumberEntity::getMain)
                        .findFirst().orElseThrow(() -> new ValidationException("Не найден главный цвет детали"))
                        .getNumber();
                String color2 = t1.getNumbers().stream().filter(PartColorNumberEntity::getMain)
                        .findFirst().orElseThrow(() -> new ValidationException("Не найден главный цвет детали"))
                        .getNumber();
                return color1.compareTo(color2);
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
                String color1 = partColorEntity.getNumbers().stream().filter(PartColorNumberEntity::getMain)
                        .findFirst().orElseThrow(() -> new ValidationException("Не найден главный цвет детали"))
                        .getNumber();
                String color2 = t1.getNumbers().stream().filter(PartColorNumberEntity::getMain)
                        .findFirst().orElseThrow(() -> new ValidationException("Не найден главный цвет детали"))
                        .getNumber();
                return color1.compareTo(color2);
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
        Assertions.assertEquals("112231", byNumber.getNumbers().stream().filter(PartColorNumberEntity::getMain)
                .findFirst().get().getNumber());
    }

    @Test
    public void getPartColorByAlternateNumber() {
        // WHEN
        PartColorEntity byNumber = partColorRepository.getPartColorByNumber("898");

        // THEN
        Assertions.assertNotNull(byNumber);
        Assertions.assertEquals("112231", byNumber.getNumbers().stream().filter(PartColorNumberEntity::getMain)
                .findFirst().get().getNumber());
        Assertions.assertTrue(byNumber.getNumbers().stream().filter(item -> !item.getMain())
                .map(PartColorNumberEntity::getNumber).collect(Collectors.toList()).containsAll(Arrays.asList("898", "899")));
    }

    @Test
    public void getPartColorByNumberPartNumber() {
        // WHEN
        List<PartColorEntity> byNumber = partColorRepository.getPartColorByNumberPartNumber("112231", "3010");

        // THEN
        Assertions.assertNotNull(byNumber);
        Assertions.assertEquals(1, byNumber.size());
        Assertions.assertTrue(byNumber.get(0).getNumbers().stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("112231")));
        Assertions.assertTrue(byNumber.get(0).getPart().getNumbers().stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("3010")));
    }

    @Test
    public void getPartColorByAlternateNumberPartNumber() {
        // WHEN
        List<PartColorEntity> byNumber = partColorRepository.getPartColorByNumberPartNumber("898", "3010");

        // THEN
        Assertions.assertNotNull(byNumber);
        Assertions.assertEquals(1, byNumber.size());
        Assertions.assertEquals("112231", byNumber.get(0).getNumbers().stream().filter(PartColorNumberEntity::getMain)
                .findFirst().get().getNumber());
        Assertions.assertTrue(byNumber.get(0).getNumbers().stream().filter(item -> !item.getMain())
                .map(item -> item.getNumber()).collect(Collectors.toList()).containsAll(Arrays.asList("898", "899")));
        Assertions.assertTrue(byNumber.get(0).getPart().getNumbers().stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("3010")));
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

    @Test
    public void getPartColorIdByPartColorNumberAndPartNumber() {
        // GIVEN
        String partNumber = "30102", colorNumber = "899";

        // WHEN
        Long partColorId = partColorRepository.getPartColorIdByPartColorNumberAndPartNumber(partNumber, colorNumber);

        // THEN
        Assertions.assertNotNull(partColorId);
    }
}
