package ru.shark.home.legomanager.datamanager;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.UserDto;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersDataManagerTest extends DaoServiceTest {
    @Autowired
    private UsersDataManager usersDataManager;

    @BeforeAll
    public void init() {
        loadUsers("UsersDataManagerTest/users.json");
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        Ordering<UserDto> ordering = new Ordering<UserDto>() {
            @Override
            public int compare(@Nullable UserDto userDto, @Nullable UserDto t1) {
                return Comparator.comparing(UserDto::getName)
                        .compare(userDto, t1);
            }
        };

        // WHEN
        PageableList<UserDto> list = usersDataManager.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 2, 2L);
        assertTrue(ordering.isOrdered(list.getData()));
    }

    @Test
    public void getAllUsers() {
        // GIVEN
        Ordering<UserDto> ordering = new Ordering<UserDto>() {
            @Override
            public int compare(@Nullable UserDto userDto, @Nullable UserDto t1) {
                return Comparator.comparing(UserDto::getName)
                        .compare(userDto, t1);
            }
        };

        // WHEN
        List<UserDto> users = usersDataManager.getAllUsers();

        // THEN
        Assertions.assertEquals(2, users.size());
        Assertions.assertTrue(ordering.isOrdered(users));
    }

    @Test
    public void save() {
        // GIVEN
        UserDto dto = prepareDto();

        // WHEN
        UserDto saved = usersDataManager.save(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(dto.getName(), saved.getName());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findUserId("Елена");

        // WHEN
        usersDataManager.deleteById(id);

        // THEN
        Assertions.assertTrue(isDeleted(id, UserEntity.class));
    }

    private UserDto prepareDto() {
        UserDto dto = new UserDto();
        dto.setName("green");

        return dto;
    }
}
