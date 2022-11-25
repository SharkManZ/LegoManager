package ru.shark.home.legomanager.datamanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.dao.dto.request.UserPartListRequest;
import ru.shark.home.legomanager.enums.UserPartRequestType;
import ru.shark.home.legomanager.util.DbTest;

import java.util.List;

public class UserPartsDataManagerTest extends DbTest {
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
        UserPartListRequest requestDto = new UserPartListRequest(UserPartRequestType.ALL, userId);

        // WHEN
        PageableList<UserPartListDto> list = userPartsDataManager.getList(requestDto, new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 3, 3L);
        List<UserPartListDto> data = list.getData();
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("112231") &&
                item.getUserCount() == 25 && item.getSetsCount() == 10));
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55531") &&
                item.getUserCount() == 3 && item.getSetsCount() == 3));
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55521") &&
                item.getUserCount() == 5 && item.getSetsCount() == 0));
    }
}
