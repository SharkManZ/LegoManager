package ru.shark.home.legomanager.datamanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.List;

public class UserPartsDataManagerTest extends DaoServiceTest {
    @Autowired
    private UserPartsDataManager userPartsDataManager;

    @BeforeAll
    public void init() {
        loadSeries("UserPartsDataManagerTest/series.json");
        loadColors("UserPartsDataManagerTest/colors.json");
        loadPartCategories("UserPartsDataManagerTest/partCats.json");
        loadParts("UserPartsDataManagerTest/parts.json");
        loadSets("UserPartsDataManagerTest/sets.json");
        loadUsers("UserPartsDataManagerTest/users.json");
    }

    @Test
    public void getList() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        List<UserPartListDto> list = userPartsDataManager.getList(userId);

        // THEN
        Assertions.assertEquals(3, list.size());
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("112231") &&
                item.getUserCount() == 25 && item.getSetsCount() == 10));
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55531") &&
                item.getUserCount() == 3 && item.getSetsCount() == 3));
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55521") &&
                item.getUserCount() == 5 && item.getSetsCount() == 0));
    }
}
