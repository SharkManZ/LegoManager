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
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColorDataManagerTest extends DaoServiceTest {
    @Autowired
    private ColorDataManager colorDataManager;

    @BeforeAll
    public void init() {
        loadColors("ColorDataManagerTest/colors.json");
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
        checkPagingDtoList(list, 2, 2L);
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
        Assertions.assertEquals(2, colors.size());
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
        Long id = entityFinder.findColorId("black");

        // WHEN
        colorDataManager.deleteById(id);

        // THEN
        Assertions.assertTrue(isDeleted(id, ColorEntity.class));
    }

    private ColorDto prepareDto() {
        ColorDto dto = new ColorDto();
        dto.setName("green");
        dto.setHexColor("#00ff00");

        return dto;
    }
}
