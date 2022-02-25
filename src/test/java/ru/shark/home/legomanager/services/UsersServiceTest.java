package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.UserDto;
import ru.shark.home.legomanager.datamanager.UsersDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UsersServiceTest extends BaseServiceTest {
    private UsersService usersService;
    private UsersDataManager usersDataManager;

    @BeforeAll
    public void init() {
        usersDataManager = mock(UsersDataManager.class);
        usersService = new UsersService();
        usersService.setUsersDataManager(usersDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(usersDataManager);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest request = new PageRequest(0, 10);
        PageableList<UserDto> pageList = new PageableList<>(Arrays.asList(new UserDto()), 1L);
        when(usersDataManager.getWithPagination(any(RequestCriteria.class))).thenReturn(pageList);

        // WHEN
        BaseResponse response = usersService.getList(request);

        // THEN
        checkPagingResponse(response);
        verify(usersDataManager, times(1)).getWithPagination(any(RequestCriteria.class));
    }

    @Test
    public void getAllList() {
        // GIVEN
        when(usersDataManager.getAllUsers()).thenReturn(new ArrayList<>());

        // WHEN
        BaseResponse allList = usersService.getAllList();

        // THEN
        checkResponse(allList);
        verify(usersDataManager, times(1)).getAllUsers();
    }

    @Test
    public void save() {
        // GIVEN
        when(usersDataManager.save(any(UserDto.class))).thenReturn(new UserDto());
        // WHEN
        BaseResponse response = usersService.save(new UserDto());

        // THEN
        checkResponseWithBody(response);
        verify(usersDataManager, times(1)).save(any(UserDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = usersService.delete(1L);

        // THEN
        checkResponse(response);
        verify(usersDataManager, times(1)).deleteById(anyLong());
    }
}
