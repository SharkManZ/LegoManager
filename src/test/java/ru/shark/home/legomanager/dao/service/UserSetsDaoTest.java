package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.dao.entity.UserSetEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;

public class UserSetsDaoTest extends DbTest {
    @Autowired
    private UserSetsDao userSetsDao;

    @BeforeAll
    public void init() {
        loadSeries("UserSetsDaoTest/series.json");
        loadSets("UserSetsDaoTest/sets.json");
        loadUsers("UserSetsDaoTest/users.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<UserSetEntity> ordering = new Ordering<UserSetEntity>() {
            @Override
            public int compare(@Nullable UserSetEntity userSetEntity, @Nullable UserSetEntity t1) {
                return userSetEntity.getSet().getName().compareTo(t1.getSet().getName());
            }
        };
        Long userId = entityFinder.findUserId("Максим");
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        //requestCriteria.setFilters(Arrays.asList(new RequestFilter("set.series.name", FieldType.STRING, "=", "Technic")));

        // WHEN
        PageableList<UserSetEntity> list = userSetsDao.getWithPagination(userId, requestCriteria);

        // THEN
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.getData().size());
        Assertions.assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        Long userId = entityFinder.findUserId("Елена");
        Long setId = entityFinder.findSetId("60296");
        UserSetEntity entity = prepareEntity(userId, setId);

        // WHEN
        UserSetEntity saved = userSetsDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getUser().getId(), saved.getUser().getId());
        Assertions.assertEquals(entity.getSet().getId(), saved.getSet().getId());
        Assertions.assertEquals(entity.getCount(), saved.getCount());
    }

    @Test
    public void saveWithCreateWithExistsByName() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");
        SetEntity setEntity = entityFinder.findSet("42082");
        UserSetEntity entity = prepareEntity(userId, setEntity.getId());

        // WHEN
        IllegalArgumentException validationException = assertThrows(IllegalArgumentException.class, () -> userSetsDao.save(entity));

        // THEN
        Assertions.assertNotNull(validationException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                UserSetEntity.getDescription(), setEntity.getName()), validationException.getMessage());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");
        Long setId = entityFinder.findSetId("42082");
        UserSetEntity entity = prepareEntity(userId, setId);
        entity.setId(entityFinder.findUserSetId("Максим", "42082"));
        entity.setCount(3);

        // WHEN
        UserSetEntity saved = userSetsDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getUser().getId(), saved.getUser().getId());
        Assertions.assertEquals(entity.getSet().getId(), saved.getSet().getId());
        Assertions.assertEquals(entity.getCount(), saved.getCount());
    }

    @Test
    public void saveWithUpdateWithExists() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");
        SetEntity setEntity = entityFinder.findSet("42082");
        UserSetEntity entity = prepareEntity(userId, setEntity.getId());
        entity.setId(entityFinder.findUserSetId("Максим", "42100"));

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> userSetsDao.save(entity));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                UserSetEntity.getDescription(), setEntity.getName()), illegalArgumentException.getMessage());
    }

    @Test
    public void saveWithUserNotFound() {
        // GIVEN
        SetEntity setEntity = entityFinder.findSet("42082");
        UserSetEntity entity = prepareEntity(999L, setEntity.getId());

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> userSetsDao.save(entity));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, UserEntity.getDescription(),
                999L), illegalArgumentException.getMessage());
    }

    @Test
    public void saveWithSetNotFound() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");
        UserSetEntity entity = prepareEntity(userId, 999L);

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> userSetsDao.save(entity));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, SetEntity.getDescription(),
                999L), illegalArgumentException.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findUserSetId("Максим", "42100");

        // WHEN
        userSetsDao.deleteById(id);

        // THEN
        Assertions.assertTrue(isDeleted(id, UserSetEntity.class));
    }

    @Test
    public void deleteByIdWithNotFound() {
        // GIVEN
        Long id = 999L;

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> userSetsDao.deleteById(id));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                UserSetEntity.getDescription(), id), illegalArgumentException.getMessage());
    }

    private UserSetEntity prepareEntity(Long userId, Long setId) {
        UserSetEntity entity = new UserSetEntity();
        entity.setUser(new UserEntity());
        entity.getUser().setId(userId);
        entity.setSet(new SetEntity());
        entity.getSet().setId(setId);
        entity.setCount(1);

        return entity;
    }
}
