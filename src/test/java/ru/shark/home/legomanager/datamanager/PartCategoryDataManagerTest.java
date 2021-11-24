package ru.shark.home.legomanager.datamanager;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.PartCategoryDto;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartCategoryDataManagerTest extends DaoServiceTest {
    @Autowired
    private PartCategoryDataManager partCategoryDataManager;

    @BeforeAll
    public void init() {
        loadPartCategories("PartCategoryDataManagerTest/partCats.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<PartCategoryDto> ordering = new Ordering<PartCategoryDto>() {
            @Override
            public int compare(@Nullable PartCategoryDto partCategoryDto, @Nullable PartCategoryDto t1) {
                return Comparator.comparing(PartCategoryDto::getName)
                        .compare(partCategoryDto, t1);
            }
        };

        // WHEN
        PageableList<PartCategoryDto> list = partCategoryDataManager.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 2, 2L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void save() {
        // GIVEN
        PartCategoryDto dto = prepareDto();

        // WHEN
        PartCategoryDto saved = partCategoryDataManager.save(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(dto.getName(), saved.getName());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findPartCategoryId("Tile");

        // WHEN
        partCategoryDataManager.deleteById(id);

        // THEN
        Assertions.assertTrue(isDeleted(id, PartCategoryEntity.class));
    }

    @Test
    public void getAllCategories() {
        // GIVEN
        Ordering<PartCategoryDto> ordering = new Ordering<PartCategoryDto>() {
            @Override
            public int compare(@Nullable PartCategoryDto partCategoryDto, @Nullable PartCategoryDto t1) {
                return Comparator.comparing(PartCategoryDto::getName)
                        .compare(partCategoryDto, t1);
            }
        };

        // WHEN
        List<PartCategoryDto> allSeries = partCategoryDataManager.getAllCategories();

        // THEN
        Assertions.assertEquals(2L, allSeries.size());
        Assertions.assertTrue(ordering.isOrdered(allSeries));
    }

    private PartCategoryDto prepareDto() {
        PartCategoryDto dto = new PartCategoryDto();
        dto.setName("Plate");

        return dto;
    }
}
