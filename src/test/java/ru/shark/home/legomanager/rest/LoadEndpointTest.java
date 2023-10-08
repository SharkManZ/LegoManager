package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
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

    @Test
    public void loadSetParts() {
        // WHEN
        Response response = loadEndpoint.loadSetParts("123");

        // THEN
        checkResponse(response);
        verify(loadService, times(1)).loadSetParts(anyString());
    }

    @Test
    public void partLoadSkipList() {
        // WHEN
        Response response = loadEndpoint.partLoadSkipList(new PageRequest(0, 10));

        // THEN
        checkResponse(response);
        verify(loadService, times(1)).getPartLoadSkipList(any(PageRequest.class));
    }

    @Test
    public void partLoadSkipSave() {
        // WHEN
        Response response = loadEndpoint.partLoadSkipSave(new PartLoadSkipDto());

        // THEN
        checkResponse(response);
        verify(loadService, times(1)).partLoadSkipSave(any(PartLoadSkipDto.class));
    }
}
