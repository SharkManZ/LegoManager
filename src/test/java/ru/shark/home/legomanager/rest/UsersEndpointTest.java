package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.UserDto;
import ru.shark.home.legomanager.services.UsersService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UsersEndpointTest extends BaseEndpointTest {
    private UsersEndpoint usersEndpoint;
    private UsersService usersService;

    @BeforeAll
    public void init() {
        usersService = mock(UsersService.class);
        usersEndpoint = new UsersEndpoint();
        usersEndpoint.setUsersService(usersService);
    }

    @BeforeEach
    public void initMethod() {
        reset(usersService);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest pageRequest = new PageRequest(0, 10);

        // WHEN
        Response response = usersEndpoint.getList(pageRequest);

        // THEN
        checkResponse(response);
        verify(usersService, times(1)).getList(any(PageRequest.class));
    }

    @Test
    public void getAllList() {
        // WHEN
        Response response = usersEndpoint.getAllList();

        // THEN
        checkResponse(response);
        verify(usersService, times(1)).getAllList();
    }

    @Test
    public void save() {
        // WHEN
        Response response = usersEndpoint.save(new UserDto());

        // THEN
        checkResponse(response);
        verify(usersService, times(1)).save(any(UserDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = usersEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(usersService, times(1)).delete(anyLong());
    }

    @Test
    public void getSetsSummary() {
        // WHEN
        Response response = usersEndpoint.getSetsSummary(1L);

        // THEN
        checkResponse(response);
        verify(usersService, times(1)).getUserSetsSummary(anyLong());
    }
}
