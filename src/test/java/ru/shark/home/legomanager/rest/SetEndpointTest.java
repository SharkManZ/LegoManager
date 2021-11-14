package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.services.SetService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SetEndpointTest extends BaseEndpointTest {
    private SetEndpoint setEndpoint;
    private SetService service;

    @BeforeAll
    public void init() {
        service = mock(SetService.class);
        setEndpoint = new SetEndpoint();
        setEndpoint.setService(service);
    }

    @BeforeEach
    public void initMethod() {
        reset(service);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest pageRequest = new PageRequest(1, 10);

        // WHEN
        Response response = setEndpoint.getList(pageRequest);

        // THEN
        checkResponse(response);
        verify(service, times(1)).getList(any(PageRequest.class));
    }
}
