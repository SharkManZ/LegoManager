package ru.shark.home.legomanager.datamanager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import static org.junit.jupiter.api.Assertions.*;

public class SeriesDataManagerTest extends DaoServiceTest {
    @Autowired
    private SeriesDataManager seriesDataManager;

    @BeforeAll
    public void init() {
        loadSeries("SeriesDataManagerTest/series.json");
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
        Long id = entityFinder.findSeriesId("technic");

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
