package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.SetDto;
import ru.shark.home.legomanager.services.SetPartService;
import ru.shark.home.legomanager.services.SetService;
import ru.shark.home.legomanager.services.dto.SearchDto;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SetEndpointTest extends BaseEndpointTest {
    private SetEndpoint setEndpoint;
    private SetService service;
    private SetPartService setPartService;

    @BeforeAll
    public void init() {
        service = mock(SetService.class);
        setPartService = mock(SetPartService.class);
        setEndpoint = new SetEndpoint();
        setEndpoint.setService(service);
        setEndpoint.setSetPartService(setPartService);
    }

    @BeforeEach
    public void initMethod() {
        reset(service, setPartService);
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

    @Test
    public void save() {
        // WHEN
        Response response = setEndpoint.save(new SetDto());

        // WHEN
        checkResponse(response);
        verify(service, times(1)).save(any(SetDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = setEndpoint.delete(1L);

        // WHEN
        checkResponse(response);
        verify(service, times(1)).delete(eq(1L));
    }

    @Test
    public void getPartsList() {
        // GIVEN
        when(setPartService.getListBySetId(anyLong(), any(PageRequest.class))).thenReturn(new BaseResponse());

        // WHEN
        Response response = setEndpoint.getPartsList(1L, new PageRequest());

        // THEN
        checkResponse(response);
        verify(setPartService, times(1)).getListBySetId(anyLong(), any(PageRequest.class));
    }

    @Test
    public void getSummary() {
        // GIVEN
        when(service.getSummary(anyLong())).thenReturn(new BaseResponse());

        // WHEN
        Response response = setEndpoint.getSummary(1L);

        // THEN
        checkResponse(response);
        verify(service, times(1)).getSummary(eq(1L));
    }

    @Test
    public void getColors() {
        // GIVEN
        when(service.getSetColors(anyLong())).thenReturn(new BaseResponse());

        // WHEN
        Response response = setEndpoint.getColors(1L);

        // THEN
        checkResponse(response);
        verify(service, times(1)).getSetColors(eq(1L));
    }

    @Test
    public void getPartCategories() {
        // GIVEN
        when(service.getSetColors(anyLong())).thenReturn(new BaseResponse());

        // WHEN
        Response response = setEndpoint.getPartCategories(1L);

        // THEN
        checkResponse(response);
        verify(service, times(1)).getSetPartCategories(eq(1L));
    }

    @Test
    public void search() {
        // GIVEN
        when(service.search(any(SearchDto.class))).thenReturn(new BaseResponse());

        // WHEN
        Response response = setEndpoint.search(new SearchDto());

        // THEN
        checkResponse(response);
        verify(service, times(1)).search(any(SearchDto.class));
    }
}
