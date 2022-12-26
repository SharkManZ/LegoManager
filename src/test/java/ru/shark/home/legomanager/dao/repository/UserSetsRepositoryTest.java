package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.UserSetEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.List;

public class UserSetsRepositoryTest extends DbTest {

    @Autowired
    private UserSetsRepository userSetsRepository;

    @BeforeAll
    public void init() {
        loadSeries("UserSetsRepositoryTest/series.json");
        loadSets("UserSetsRepositoryTest/sets.json");
        loadUsers("UserSetsRepositoryTest/users.json");
    }

    @Test
    public void findUserSetByUserAndSet() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        UserSetEntity found = userSetsRepository.findUserSetByUserAndSet(userId, setId);

        // THEN
        Assertions.assertNotNull(found);
        Assertions.assertEquals(userId, found.getUser().getId());
        Assertions.assertEquals(setId, found.getSet().getId());
    }

    @Test
    public void getUserSetsByUser() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        List<UserSetEntity> list = userSetsRepository.getUserSetsByUser(userId);

        // THEN
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void getUserSeriesCountByUserId() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        Long result = userSetsRepository.getUserSeriesCountByUserId(userId);

        // THEN
        Assertions.assertEquals(1L, result);
    }

    @Test
    public void getUserSetsCountByUserId() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        Long result = userSetsRepository.getUserSetsCountByUserId(userId);

        // THEN
        Assertions.assertEquals(3L, result);
    }
}
