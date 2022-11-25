package ru.shark.home.legomanager.dao.repository;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.SetPartEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.List;

public class SetPartRepositoryTest extends DbTest {

    @Autowired
    private SetPartRepository setPartRepository;

    @BeforeAll
    public void init() {
        loadSeries("SetPartRepositoryTest/series.json");
        loadColors("SetPartRepositoryTest/colors.json");
        loadPartCategories("SetPartRepositoryTest/partCats.json");
        loadParts("SetPartRepositoryTest/parts.json");
        loadSets("SetPartRepositoryTest/sets.json");
    }

    @Test
    public void getSetPartsBySetId() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");
        // GIVEN
        Ordering<SetPartEntity> ordering = new Ordering<SetPartEntity>() {
            @Override
            public int compare(@Nullable SetPartEntity setPartEntity, @Nullable SetPartEntity t1) {
                return setPartEntity.getId().compareTo(t1.getId());
            }
        };

        // WHEN
        List<SetPartEntity> list = setPartRepository.getSetPartsBySetId(setId);

        // THEN
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(ordering.reverse().isOrdered(list));
    }

    @Test
    public void getSetPartBySetAndPrtColorId() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");
        Long partColorId = entityFinder.findPartColorId("112231");

        // WHEN
        SetPartEntity setPartBySetAndPrtColorId = setPartRepository.getSetPartBySetAndPrtColorId(setId, partColorId);

        // THEN
        Assertions.assertNotNull(setPartBySetAndPrtColorId);
        Assertions.assertEquals(setId, setPartBySetAndPrtColorId.getSet().getId());
        Assertions.assertEquals(partColorId, setPartBySetAndPrtColorId.getPartColor().getId());
    }

    @Test
    public void getAllSetsPartsCount() {
        // GIVEN
        Long expected = 13L;

        // WHEN
        Long count = setPartRepository.getAllSetsPartsCount();

        // THEN
        Assertions.assertEquals(expected, count);
    }

    @Test
    public void getSetPartsCount() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");
        Long expected = 2L;

        // WHEN
        Long setPartsCount = setPartRepository.getSetPartsCount(setId);

        // THEN
        Assertions.assertEquals(expected, setPartsCount);
    }

    @Test
    public void findBySetId() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        List<SetPartEntity> list = setPartRepository.findBySetId(setId);

        // THEN
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(list.stream().allMatch(item -> item.getSet().getId().equals(setId)));
    }
}
