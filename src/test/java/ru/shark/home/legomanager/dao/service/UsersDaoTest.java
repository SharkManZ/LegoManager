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
import ru.shark.home.legomanager.dao.dto.UserSetsSummaryDto;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;

public class UsersDaoTest extends DaoServiceTest {
    @Autowired
    private UsersDao usersDao;

    @BeforeAll
    public void init() {
        loadSeries("UsersDaoTest/series.json");
        loadColors("UsersDaoTest/colors.json");
        loadPartCategories("UsersDaoTest/partCats.json");
        loadParts("UsersDaoTest/parts.json");
        loadSets("UsersDaoTest/sets.json");
        loadUsers("UsersDaoTest/users.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<UserEntity> ordering = new Ordering<UserEntity>() {
            @Override
            public int compare(@Nullable UserEntity userEntity, @Nullable UserEntity t1) {
                return Comparator.comparing(UserEntity::getName)
                        .compare(userEntity, t1);
            }
        };

        // WHEN
        PageableList<UserEntity> list = usersDao.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingList(list, 2, 2L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void getWithPaginationWithSearch() {
        // GIVEN
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch(new RequestSearch("Еле", false));

        // WHEN
        PageableList<UserEntity> list = usersDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingList(list, 1, 1L);
    }

    @Test
    public void getWithPaginationWithSearchEquals() {
        // GIVEN
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);
        requestCriteria.setSearch(new RequestSearch("Максим", true));

        // WHEN
        PageableList<UserEntity> list = usersDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingList(list, 1, 1L);
    }

    @Test
    public void getAllUsers() {
        // GIVEN
        Ordering<UserEntity> ordering = new Ordering<UserEntity>() {
            @Override
            public int compare(@Nullable UserEntity userEntity, @Nullable UserEntity t1) {
                return Comparator.comparing(UserEntity::getName)
                        .compare(userEntity, t1);
            }
        };

        // WHEN
        List<UserEntity> users = usersDao.getAllUsers();

        // THEN
        Assertions.assertEquals(2, users.size());
        Assertions.assertTrue(ordering.isOrdered(users));
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        UserEntity entity = prepareEntity();

        // WHEN
        UserEntity saved = usersDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
    }

    @Test
    public void saveWithCreateWithExistsByName() {
        // GIVEN
        UserEntity entity = prepareEntity();
        entity.setName("Елена");

        // WHEN
        IllegalArgumentException validationException = assertThrows(IllegalArgumentException.class, () -> usersDao.save(entity));

        // THEN
        Assertions.assertNotNull(validationException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                UserEntity.getDescription(), entity.getName()), validationException.getMessage());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        UserEntity entity = prepareEntity();
        entity.setId(entityFinder.findUserId("Максим"));

        // WHEN
        UserEntity saved = usersDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getName(), saved.getName());
    }

    @Test
    public void saveWithUpdateWithExists() {
        // GIVEN
        UserEntity entity = prepareEntity();
        entity.setId(entityFinder.findUserId("Максим"));
        entity.setName("Елена");

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> usersDao.save(entity));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                UserEntity.getDescription(), entity.getName()), illegalArgumentException.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findUserId("Елена");

        // WHEN
        usersDao.deleteById(id);

        // THEN
        Assertions.assertTrue(isDeleted(id, UserEntity.class));
    }

    @Test
    public void deleteByIdWithNotFound() {
        // GIVEN
        Long id = 999L;

        // WHEN
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> usersDao.deleteById(id));

        // THEN
        Assertions.assertNotNull(illegalArgumentException);
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                UserEntity.getDescription(), id), illegalArgumentException.getMessage());
    }

    @Test
    public void getUserSetsSummary() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        UserSetsSummaryDto summary = usersDao.getUserSetsSummary(userId);

        // THEN
        Assertions.assertNotNull(summary);
        Assertions.assertEquals(13L, summary.getPartsCount());
        Assertions.assertEquals(2L, summary.getColorsCount());
        Assertions.assertEquals(2L, summary.getUniquePartsCount());
    }

    private UserEntity prepareEntity() {
        UserEntity entity = new UserEntity();
        entity.setName("July");

        return entity;
    }
}
