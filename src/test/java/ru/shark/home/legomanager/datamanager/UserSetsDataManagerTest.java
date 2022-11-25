package ru.shark.home.legomanager.datamanager;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.UserSetDto;
import ru.shark.home.legomanager.util.DbTest;

public class UserSetsDataManagerTest extends DbTest {

    @Autowired
    private UserSetsDataManager userSetsDataManager;

    @BeforeAll
    public void init() {
        loadSeries("UserSetsDataManagerTest/series.json");
        loadSets("UserSetsDataManagerTest/sets.json");
        loadUsers("UserSetsDataManagerTest/users.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<UserSetDto> ordering = new Ordering<UserSetDto>() {
            @Override
            public int compare(@Nullable UserSetDto userSetDto, @Nullable UserSetDto t1) {
                return userSetDto.getSet().getName().compareTo(t1.getSet().getName());
            }
        };
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        PageableList<UserSetDto> list = userSetsDataManager.getWithPagination(userId, new RequestCriteria(0, 10));

        // THEN
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.getData().size());
        Assertions.assertTrue(ordering.isOrdered(list.getData()));
    }
}
