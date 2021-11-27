package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.PartFullDto;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.shark.home.common.common.ErrorConstants.*;

public class PartDaoTest extends DaoServiceTest {

    @Autowired
    private PartDao partDao;

    @BeforeAll
    public void init() {
        loadPartCategories("PartDaoTest/partCats.json");
        loadColors("PartDaoTest/colors.json");
        loadParts("PartDaoTest/parts.json");
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
        Map<Long, Pair<Integer, String>> counts = new HashMap<>();
        counts.put(entityFinder.findPartId("3010"), Pair.of(2, "112231"));
        counts.put(entityFinder.findPartId("3001"), Pair.of(0, null));


        // WHEN
        PageableList<PartFullDto> list = partDao.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 2, 2L);
        assertTrue(ordering.isOrdered(list.getData()));
        list.getData().forEach(item -> {
            Assertions.assertEquals(counts.get(item.getId()).getLeft(), item.getColorsCount());
            Assertions.assertEquals(counts.get(item.getId()).getRight(), item.getMinColorNumber());
        });
    }

    @Test
    public void getWithPaginationWithSearch() {
        // GIVEN
        Ordering<PartFullDto> ordering = new Ordering<PartFullDto>() {
            @Override
            public int compare(@Nullable PartFullDto partDto, @Nullable PartFullDto t1) {
                return Comparator.comparing(PartFullDto::getName)
                        .compare(partDto, t1);
            }
        };
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch("300");

        // WHEN
        PageableList<PartFullDto> list = partDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        PartEntity entity = prepareEntity();

        // WHEN
        PartEntity saved = partDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
        Assertions.assertEquals(entity.getNumber(), saved.getNumber());
        Assertions.assertEquals(entity.getCategory().getId(), saved.getCategory().getId());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        PartEntity entity = prepareEntity();
        entity.setId(entityFinder.findPartId("3010"));

        // WHEN
        PartEntity saved = partDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
        Assertions.assertEquals(entity.getNumber(), saved.getNumber());
        Assertions.assertEquals(entity.getCategory().getId(), saved.getCategory().getId());
    }

    @Test
    public void saveWithCreateAndExists() {
        // GIVEN
        PartEntity entity = prepareEntity();
        entity.setNumber("3010");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> partDao.save(entity));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartEntity.getDescription(),
                entity.getNumber()), validationException.getMessage());
    }

    @Test
    public void saveWithUpdateAndExists() {
        // GIVEN
        PartEntity entity = prepareEntity();
        entity.setId(entityFinder.findPartId("3010"));
        entity.setNumber("3001");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> partDao.save(entity));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartEntity.getDescription(),
                entity.getNumber()), validationException.getMessage());
    }

    @Test
    public void saveWithValidation() {
        // GIVEN
        PartEntity entityNoName = prepareEntity();
        entityNoName.setName(null);
        PartEntity entityNoNumber = prepareEntity();
        entityNoNumber.setNumber(null);
        PartEntity entityNoCategory = prepareEntity();
        entityNoCategory.setCategory(null);
        PartEntity entityNoCategoryId = prepareEntity();
        entityNoCategoryId.getCategory().setId(null);
        PartEntity entityCategoryNotFound = prepareEntity();
        entityCategoryNotFound.getCategory().setId(999L);

        // WHEN
        ValidationException noName = assertThrows(ValidationException.class, () -> partDao.save(entityNoName));
        ValidationException noNumber = assertThrows(ValidationException.class, () -> partDao.save(entityNoNumber));
        ValidationException noCategory = assertThrows(ValidationException.class, () -> partDao.save(entityNoCategory));
        ValidationException noCategoryId = assertThrows(ValidationException.class, () -> partDao.save(entityNoCategoryId));
        ValidationException categoryNotFound = assertThrows(ValidationException.class, () -> partDao.save(entityCategoryNotFound));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "name",
                PartEntity.getDescription()), noName.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                PartEntity.getDescription()), noNumber.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "category",
                PartEntity.getDescription()), noCategory.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "category",
                PartEntity.getDescription()), noCategoryId.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartCategoryEntity.getDescription(),
                entityCategoryNotFound.getCategory().getId()), categoryNotFound.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long setId = entityFinder.findPartId("3010");

        // WHEN
        partDao.deleteById(setId);

        // THEN
        isDeleted(setId, SetEntity.class);
    }

    private PartEntity prepareEntity() {
        PartEntity entity = new PartEntity();
        entity.setCategory(new PartCategoryEntity());
        entity.getCategory().setId(entityFinder.findPartCategoryId("brick"));
        entity.setName("plate 1x10");
        entity.setNumber("4488");
        return entity;
    }
}
