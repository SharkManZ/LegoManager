package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.UserSetEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.List;

public class UserSetsRepositoryTest extends DaoServiceTest {

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
    public void getAllByUser() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        List<UserSetEntity> list = userSetsRepository.getAllByUser(userId);

        // THEN
        Assertions.assertEquals(2, list.size());
    }
}
