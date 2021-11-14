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
import ru.shark.home.legomanager.dao.dto.SeriesFullDto;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class SeriesDataManagerTest extends DaoServiceTest {
    @Autowired
    private SeriesDataManager seriesDataManager;

    @BeforeAll
    public void init() {
        loadSeries("SeriesDataManagerTest/series.json");
        loadSets("SeriesDataManagerTest/sets.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<SeriesFullDto> ordering = new Ordering<SeriesFullDto>() {
            @Override
            public int compare(@Nullable SeriesFullDto seriesEntity, @Nullable SeriesFullDto t1) {
                return Comparator.comparing(SeriesFullDto::getName)
                        .compare(seriesEntity, t1);
            }
        };

        // WHEN
        PageableList<SeriesFullDto> list = seriesDataManager.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 4, 4L);
        assertTrue(ordering.isOrdered(list.getData()));
        boolean setsCountChecked = false;
        for (SeriesFullDto item : list.getData()) {
            if (item.getName().equalsIgnoreCase("technic")) {
                Assertions.assertEquals(2, item.getSetsCount());
                setsCountChecked = true;
            }
            Assertions.assertEquals(item.getImgName(), item.getName()
                    .toLowerCase()
                    .replaceAll(" ", "_")
                    .replaceAll("-", "_"));
        }

        Assertions.assertTrue(setsCountChecked);
    }

    @Test
    public void findById() {
        // GIVEN
        SeriesEntity entity = entityFinder.findSeries("technic");

        // WHEN
        SeriesDto dto = seriesDataManager.findById(entity.getId());

        // THEN
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    public void save() {
        // GIVEN
        SeriesDto dto = prepareDto(entityFinder.findSeriesId("technic"), "technic");

        // WHEN
        SeriesDto saved = seriesDataManager.save(dto);

        // THEN
        assertNotNull(dto);
        assertEquals(dto.getId(), saved.getId());
        assertEquals(dto.getName(), saved.getName());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findSeriesId("Speed Champions");

        // WHEN
        seriesDataManager.deleteById(id);

        // THEN
        assertTrue(isDeleted(id, SeriesEntity.class));
    }

    private SeriesDto prepareDto(Long id, String name) {
        SeriesDto dto = new SeriesDto();
        dto.setId(id);
        dto.setName(name);

        return dto;
    }
}
