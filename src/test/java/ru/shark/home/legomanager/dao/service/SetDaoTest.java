package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.SeriesFullDto;
import ru.shark.home.legomanager.dao.dto.SetFullDto;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetDaoTest extends DaoServiceTest {

    @Autowired
    private SetDao setDao;

    @BeforeAll
    public void init() {
        loadSeries("SetDaoTest/series.json");
        loadSets("SetDaoTest/sets.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<SetFullDto> ordering = new Ordering<SetFullDto>() {
            @Override
            public int compare(@Nullable SetFullDto setDto, @Nullable SetFullDto t1) {
                return Comparator.comparing(SetFullDto::getName)
                        .compare(setDto, t1);
            }
        };

        // WHEN
        PageableList<SetFullDto> list = setDao.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 3, 3L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void getWithPaginationWithSearch() {
        // GIVEN
        Ordering<SetFullDto> ordering = new Ordering<SetFullDto>() {
            @Override
            public int compare(@Nullable SetFullDto setDto, @Nullable SetFullDto t1) {
                return Comparator.comparing(SetFullDto::getName)
                        .compare(setDto, t1);
            }
        };
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch("421");

        // WHEN
        PageableList<SetFullDto> list = setDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        assertTrue(ordering.isOrdered(list.getData()));
    }
}
