package ru.shark.home.legomanager.datamanager;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColorDataManagerTest extends DbTest {
    @Autowired
    private ColorDataManager colorDataManager;

    @BeforeAll
    public void init() {
        loadColors("ColorDataManagerTest/colors.json");
        loadPartCategories("ColorDataManagerTest/partCats.json");
        loadParts("ColorDataManagerTest/parts.json");
        loadSeries("ColorDataManagerTest/series.json");
        loadSets("ColorDataManagerTest/sets.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<ColorDto> ordering = new Ordering<ColorDto>() {
            @Override
            public int compare(@Nullable ColorDto colorDto, @Nullable ColorDto t1) {
                return Comparator.comparing(ColorDto::getName)
                        .compare(colorDto, t1);
            }
        };

        // WHEN
        PageableList<ColorDto> list = colorDataManager.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 3, 3L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void getAllColors() {
        // GIVEN
        Ordering<ColorDto> ordering = new Ordering<ColorDto>() {
            @Override
            public int compare(@Nullable ColorDto colorDto, @Nullable ColorDto t1) {
                return Comparator.comparing(ColorDto::getName)
                        .compare(colorDto, t1);
            }
        };

        // WHEN
        List<ColorDto> colors = colorDataManager.getAllColors();

        // THEN
        Assertions.assertEquals(3, colors.size());
        Assertions.assertTrue(ordering.isOrdered(colors));
    }

    @Test
    public void save() {
        // GIVEN
        ColorDto dto = prepareDto();

        // WHEN
        ColorDto saved = colorDataManager.save(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(dto.getName(), saved.getName());
        Assertions.assertEquals(dto.getHexColor(), saved.getHexColor());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findColorId("Reddish Brown");

        // WHEN
        colorDataManager.deleteById(id);

        // THEN
        Assertions.assertTrue(isDeleted(id, ColorEntity.class));
    }

    @Test
    public void getColorsBySetId() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        List<ColorDto> colors = colorDataManager.getListBySetId(setId);

        // THEN
        Assertions.assertEquals(2, colors.size());
    }

    @Test
    public void getPartNotExistsColors() {
        // GIVEN
        Long partId = entityFinder.findPartId("3010");

        // WHEN
        List<ColorDto> list = colorDataManager.getPartNotExistsColors(partId);

        // THEN
        Assertions.assertEquals(1L, list.size());
    }

    private ColorDto prepareDto() {
        ColorDto dto = new ColorDto();
        dto.setName("green");
        dto.setHexColor("#00ff00");

        return dto;
    }
}
