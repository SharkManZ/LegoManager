package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.UserSetsSummaryDto;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.List;

public class UsersRepositoryTest extends DaoServiceTest {
    @Autowired
    private UsersRepository usersRepository;

    @BeforeAll
    public void init() {
        loadSeries("UsersRepositoryTest/series.json");
        loadColors("UsersRepositoryTest/colors.json");
        loadPartCategories("UsersRepositoryTest/partCats.json");
        loadParts("UsersRepositoryTest/parts.json");
        loadSets("UsersRepositoryTest/sets.json");
        loadUsers("UsersRepositoryTest/users.json");
    }

    @Test
    public void getAllUsers() {
        // WHEN
        List<UserEntity> allUsers = usersRepository.getAllUsers();

        // THEN
        Assertions.assertEquals(2, allUsers.size());
    }

    @Test
    public void findUserByName() {
        // WHEN
        UserEntity byName = usersRepository.findUserByName("Максим");

        // THEN
        Assertions.assertNotNull(byName);
        Assertions.assertEquals("Максим", byName.getName());
    }

    @Test
    public void getUserSetsSummary() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        UserSetsSummaryDto summary = usersRepository.getUserSetsSummary(userId);

        // THEN
        Assertions.assertNotNull(summary);
        Assertions.assertEquals(13L, summary.getPartsCount());
        Assertions.assertEquals(2L, summary.getColorsCount());
        Assertions.assertEquals(2L, summary.getUniquePartsCount());
    }
}
