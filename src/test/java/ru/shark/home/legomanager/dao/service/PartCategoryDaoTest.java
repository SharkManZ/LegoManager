package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;
import static ru.shark.home.legomanager.common.ErrorConstants.PART_CATEGORY_DELETE_WITH_SETS;

public class PartCategoryDaoTest extends DaoServiceTest {

    @Autowired
    private PartCategoryDao partCategoryDao;

    @BeforeAll
    public void init() {
        loadPartCategories("PartCategoryDaoTest/partCats.json");
        loadParts("PartCategoryDaoTest/parts.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<PartCategoryEntity> ordering = new Ordering<PartCategoryEntity>() {
            @Override
            public int compare(@Nullable PartCategoryEntity partCategoryEntity, @Nullable PartCategoryEntity t1) {
                return Comparator.comparing(PartCategoryEntity::getName)
                        .compare(partCategoryEntity, t1);
            }
        };

        // WHEN
        PageableList<PartCategoryEntity> list = partCategoryDao.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingList(list, 2, 2L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void getWithPaginationWithSearch() {
        // GIVEN
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch("bri");

        // WHEN
        PageableList<PartCategoryEntity> list = partCategoryDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingList(list, 1, 1L);
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        PartCategoryEntity entity = prepareEntity();

        // WHEN
        PartCategoryEntity saved = partCategoryDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
    }

    @Test
    public void saveWithCreateWithExistsByName() {
        // GIVEN
        PartCategoryEntity entity = prepareEntity();
        entity.setName("Tile");

        // WHEN
        IllegalArgumentException validationException = assertThrows(IllegalArgumentException.class, () -> partCategoryDao.save(entity));

        // THEN
        Assertions.assertNotNull(validationException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                PartCategoryEntity.getDescription(), entity.getName()), validationException.getMessage());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        PartCategoryEntity entity = prepareEntity();
        entity.setId(entityFinder.findPartCategoryId("Tile"));

        // WHEN
        PartCategoryEntity saved = partCategoryDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
    }

    @Test
    public void saveWithUpdateWithExists() {
        // GIVEN
        PartCategoryEntity entity = prepareEntity();
        entity.setId(entityFinder.findPartCategoryId("Tile"));
        entity.setName("Brick");

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partCategoryDao.save(entity));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                PartCategoryEntity.getDescription(), entity.getName()), illegalArgumentException.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findPartCategoryId("Tile");

        // WHEN
        partCategoryDao.deleteById(id);

        // THEN
        Assertions.assertTrue(isDeleted(id, PartCategoryEntity.class));
    }

    @Test
    public void deleteByIdWithNotFound() {
        // GIVEN
        Long id = 999L;

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partCategoryDao.deleteById(id));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                PartCategoryEntity.getDescription(), id), illegalArgumentException.getMessage());
    }

    @Test
    public void deleteByIdWithParts() {
        // GIVEN
        Long id = entityFinder.findPartCategoryId("Brick");

        // WHEN
        ValidationException validationException = assertThrows(ValidationException.class, () -> partCategoryDao.deleteById(id));

        // THEN
        Assertions.assertEquals(PART_CATEGORY_DELETE_WITH_SETS, validationException.getMessage());
    }

    private PartCategoryEntity prepareEntity() {
        PartCategoryEntity entity = new PartCategoryEntity();
        entity.setName("Plate");

        return entity;
    }
}
