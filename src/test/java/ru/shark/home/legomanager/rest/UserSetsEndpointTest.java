package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.UserSetDto;
import ru.shark.home.legomanager.services.UserSetsService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

public class UserSetsEndpointTest extends BaseEndpointTest {
    private UserSetsEndpoint userSetsEndpoint;
    private UserSetsService userSetsService;

    @BeforeAll
    public void init() {
        userSetsService = mock(UserSetsService.class);
        userSetsEndpoint = new UserSetsEndpoint();
        userSetsEndpoint.setUserSetsService(userSetsService);
    }

    @Test
    public void getList() {
        // WHEN
        Response response = userSetsEndpoint.getList(1L, new PageRequest());

        // THEN
        checkResponse(response);
        verify(userSetsService, times(1)).getList(anyLong(), any(PageRequest.class));
    }

    @Test
    public void save() {
        // WHEN
        Response response = userSetsEndpoint.save(new UserSetDto());

        // THEN
        checkResponse(response);
        verify(userSetsService, times(1)).save(any(UserSetDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = userSetsEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(userSetsService, times(1)).delete(anyLong());
    }
}
