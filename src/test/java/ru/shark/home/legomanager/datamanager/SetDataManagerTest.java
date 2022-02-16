package ru.shark.home.legomanager.datamanager;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.dao.dto.SetDto;
import ru.shark.home.legomanager.dao.dto.SetFullDto;
import ru.shark.home.legomanager.dao.dto.SetSummaryDto;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetDataManagerTest extends DaoServiceTest {

    @Autowired
    private SetDataManager setDataManager;

    @BeforeAll
    public void init() {
        loadColors("SetDataManagerTest/colors.json");
        loadPartCategories("SetDataManagerTest/partCats.json");
        loadParts("SetDataManagerTest/parts.json");
        loadSeries("SetDataManagerTest/series.json");
        loadSets("SetDataManagerTest/sets.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<SetFullDto> ordering = new Ordering<SetFullDto>() {
            @Override
            public int compare(@Nullable SetFullDto setDto, @Nullable SetFullDto t1) {
                return Comparator.comparing(SetFullDto::getNumber)
                        .compare(setDto, t1);
            }
        };

        // WHEN
        PageableList<SetFullDto> list = setDataManager.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 3, 3L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void getWithPaginationWithSeriesId() {
        // GIVEN
        Ordering<SetFullDto> ordering = new Ordering<SetFullDto>() {
            @Override
            public int compare(@Nullable SetFullDto setDto, @Nullable SetFullDto t1) {
                return Comparator.comparing(SetFullDto::getNumber)
                        .compare(setDto, t1);
            }
        };
        Long seriesId = entityFinder.findSeriesId("Technic");

        // WHEN
        PageableList<SetFullDto> list = setDataManager.getWithPagination(new RequestCriteria(0, 10), seriesId);

        // THEN
        checkPagingDtoList(list, 2, 2L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void save() {
        // GIVEN
        SetDto dto = new SetDto();
        dto.setName("newSet");
        dto.setNumber("123123");
        dto.setSeries(new SeriesDto());
        dto.getSeries().setId(entityFinder.findSeriesId("technic"));
        dto.setYear(2020);

        // WHEN
        SetDto saved = setDataManager.save(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(dto.getNumber(), saved.getNumber());
        Assertions.assertEquals(dto.getName(), saved.getName());
        Assertions.assertEquals(dto.getYear(), saved.getYear());
        Assertions.assertEquals(dto.getSeries().getId(), saved.getSeries().getId());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long setId = entityFinder.findSetId("60296");

        // WHEN
        setDataManager.deleteById(setId);

        // THEN
        isDeleted(setId, SetEntity.class);
    }

    @Test
    public void getSummary() {
        // GIVEN
        SetEntity set = entityFinder.findSet("42082");
        Integer expectedPartsCount = 15;
        Integer expectedUniquePartsCount = 2;

        // WHEN
        SetSummaryDto summary = setDataManager.getSummary(set.getId());

        // THEN
        Assertions.assertNotNull(summary);
        Assertions.assertEquals(summary.getNumber(), set.getNumber());
        Assertions.assertEquals(summary.getName(), set.getName());
        Assertions.assertEquals(summary.getYear(), set.getYear());
        Assertions.assertEquals(summary.getPartsCount(), expectedPartsCount);
        Assertions.assertEquals(summary.getUniquePartsCount(), expectedUniquePartsCount);
        Assertions.assertEquals(summary.getColors().size(), 2);
    }
}
