package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.legomanager.services.LoadService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

public class LoadEndpointTest extends BaseEndpointTest {
    private LoadEndpoint loadEndpoint;
    private LoadService loadService;

    @BeforeAll
    public void init() {
        loadService = mock(LoadService.class);
        loadEndpoint = new LoadEndpoint();
        loadEndpoint.setLoadService(loadService);
    }

    @BeforeEach
    public void initMethod() {
        clearInvocations(loadService);
    }

    @Test
    public void checkParts() {
        // WHEN
        Response response = loadEndpoint.checkParts("42082");

        // THEN
        checkResponse(response);
        verify(loadService, times(1)).checkParts(anyString());
    }
}
