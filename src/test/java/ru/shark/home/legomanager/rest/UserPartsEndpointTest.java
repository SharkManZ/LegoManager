package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.UserPartDto;
import ru.shark.home.legomanager.services.UserPartsService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

public class UserPartsEndpointTest extends BaseEndpointTest {
    private UserPartsEndpoint userPartsEndpoint;
    private UserPartsService userPartsService;

    @BeforeAll
    public void init() {
        userPartsEndpoint = new UserPartsEndpoint();
        userPartsService = mock(UserPartsService.class);
        userPartsEndpoint.setUserPartsService(userPartsService);
    }

    @BeforeEach
    public void initMethod() {
        reset(userPartsService);
    }

    @Test
    public void getList() {
        // WHEN
        Response response = userPartsEndpoint.getList(1L, new PageRequest());

        // THEN
        checkResponse(response);
        verify(userPartsService, times(1)).getList(anyLong(), any(PageRequest.class));
    }

    @Test
    public void save() {
        // WHEN
        Response response = userPartsEndpoint.save(new UserPartDto());

        // THEN
        checkResponse(response);
        verify(userPartsService, times(1)).save(any(UserPartDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = userPartsEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(userPartsService, times(1)).delete(anyLong());
    }
}
