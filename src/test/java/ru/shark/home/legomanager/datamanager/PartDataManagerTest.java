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
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.dao.dto.PartFullDto;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartDataManagerTest extends DbTest {

    @Autowired
    private PartDataManager partDataManager;

    @BeforeAll
    public void init() {
        loadPartCategories("PartDataManagerTest/partCats.json");
        loadParts("PartDataManagerTest/parts.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<PartFullDto> ordering = new Ordering<PartFullDto>() {
            @Override
            public int compare(@Nullable PartFullDto partDto, @Nullable PartFullDto t1) {
                return Comparator.comparing(PartFullDto::getNumber)
                        .compare(partDto, t1);
            }
        };

        // WHEN
        PageableList<PartFullDto> list = partDataManager.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 2, 2L);
        assertTrue(ordering.isOrdered(list.getData()));
    }
//
//    @Test
//    public void save() {
//        // GIVEN
//        PartDto dto = new PartDto();
//        dto.setName("plate 1x10");
//        dto.setNumber("4477");
//        dto.setAlternateNumber("9876");
//        dto.setCategory(new PartCategoryDto());
//        dto.getCategory().setId(entityFinder.findPartCategoryId("brick"));
//
//        // WHEN
//        PartDto saved = partDataManager.save(dto);
//
//        // THEN
//        Assertions.assertNotNull(saved);
//        Assertions.assertNotNull(saved.getId());
//        Assertions.assertEquals(dto.getNumber(), saved.getNumber());
//        Assertions.assertEquals(dto.getName(), saved.getName());
//        Assertions.assertEquals(dto.getCategory().getId(), saved.getCategory().getId());
//        Assertions.assertEquals(dto.getAlternateNumber(), saved.getAlternateNumber());
//    }
//
//    @Test
//    public void deleteById() {
//        // GIVEN
//        Long setId = entityFinder.findPartId("3010");
//
//        // WHEN
//        partDataManager.deleteById(setId);
//
//        // THEN
//        isDeleted(setId, PartEntity.class);
//    }
}
