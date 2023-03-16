package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.common.RequestSearch;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;

public class ColorDaoTest extends DbTest {

    @Autowired
    private ColorDao colorDao;

    @BeforeAll
    public void init() {
        loadColors("ColorDaoTest/colors.json");
        loadPartCategories("ColorDaoTest/partCats.json");
        loadParts("ColorDaoTest/parts.json");
        loadSeries("ColorDaoTest/series.json");
        loadSets("ColorDaoTest/sets.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<ColorEntity> ordering = new Ordering<ColorEntity>() {
            @Override
            public int compare(@Nullable ColorEntity colorEntity, @Nullable ColorEntity t1) {
                return Comparator.comparing(ColorEntity::getName)
                        .compare(colorEntity, t1);
            }
        };

        // WHEN
        PageableList<ColorEntity> list = colorDao.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingList(list, 3, 3L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void getWithPaginationWithSearch() {
        // GIVEN
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch(new RequestSearch("red", false));

        // WHEN
        PageableList<ColorEntity> list = colorDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingList(list, 2, 2L);
    }

    @Test
    public void getWithPaginationWithSearchEquals() {
        // GIVEN
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch(new RequestSearch("Red", true));

        // WHEN
        PageableList<ColorEntity> list = colorDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingList(list, 1, 1L);
    }

    @Test
    public void getAllColors() {
        // GIVEN
        Ordering<ColorEntity> ordering = new Ordering<ColorEntity>() {
            @Override
            public int compare(@Nullable ColorEntity colorEntity, @Nullable ColorEntity t1) {
                return Comparator.comparing(ColorEntity::getName)
                        .compare(colorEntity, t1);
            }
        };

        // WHEN
        List<ColorEntity> colors = colorDao.getAllColors();

        // THEN
        Assertions.assertEquals(3, colors.size());
        Assertions.assertTrue(ordering.isOrdered(colors));
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        ColorEntity entity = prepareEntity();

        // WHEN
        ColorEntity saved = colorDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
        Assertions.assertEquals(entity.getHexColor(), saved.getHexColor());
    }

    @Test
    public void saveWithCreateWithExistsByName() {
        // GIVEN
        ColorEntity entity = prepareEntity();
        entity.setName("Black");

        // WHEN
        IllegalArgumentException validationException = assertThrows(IllegalArgumentException.class, () -> colorDao.save(entity));

        // THEN
        Assertions.assertNotNull(validationException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                ColorEntity.getDescription(), entity.getName()), validationException.getMessage());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        ColorEntity entity = prepareEntity();
        entity.setId(entityFinder.findColorId("black"));

        // WHEN
        ColorEntity saved = colorDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
        Assertions.assertEquals(entity.getHexColor(), saved.getHexColor());
    }

    @Test
    public void saveWithUpdateWithExists() {
        // GIVEN
        ColorEntity entity = prepareEntity();
        entity.setId(entityFinder.findColorId("black"));
        entity.setName("Red");

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> colorDao.save(entity));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                ColorEntity.getDescription(), entity.getName()), illegalArgumentException.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findColorId("Reddish Brown");

        // WHEN
        colorDao.deleteById(id);

        // THEN
        Assertions.assertTrue(isDeleted(id, ColorEntity.class));
    }

    @Test
    public void deleteByIdWithNotFound() {
        // GIVEN
        Long id = 999L;

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> colorDao.deleteById(id));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                ColorEntity.getDescription(), id), illegalArgumentException.getMessage());
    }

    @Test
    public void getColorsBySetId() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        List<ColorEntity> colors = colorDao.getListBySetId(setId);

        // THEN
        Assertions.assertEquals(2, colors.size());
    }

    @Test
    public void getPartNotExistsColors() {
        // GIVEN
        Long partId = entityFinder.findPartId("3010");

        // WHEN
        List<ColorEntity> list = colorDao.getPartNotExistsColors(partId);

        // THEN
        Assertions.assertEquals(1L, list.size());
    }

    private ColorEntity prepareEntity() {
        ColorEntity entity = new ColorEntity();
        entity.setName("Green");
        entity.setHexColor("#00ff00");

        return entity;
    }
}
