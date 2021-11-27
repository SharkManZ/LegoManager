package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.services.ColorService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ColorEndpointTest extends BaseEndpointTest {
    private ColorEndpoint colorEndpoint;
    private ColorService colorService;

    @BeforeAll
    public void init() {
        colorService = mock(ColorService.class);
        colorEndpoint = new ColorEndpoint();
        colorEndpoint.setColorService(colorService);
    }

    @BeforeEach
    public void initMethod() {
        reset(colorService);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest pageRequest = new PageRequest(0, 10);

        // WHEN
        Response response = colorEndpoint.getList(pageRequest);

        // THEN
        checkResponse(response);
        verify(colorService, times(1)).getList(any(PageRequest.class));
    }

    @Test
    public void getAllList() {
        // WHEN
        Response response = colorEndpoint.getAllList();

        // THEN
        checkResponse(response);
        verify(colorService, times(1)).getAllList();
    }

    @Test
    public void save() {
        // WHEN
        Response response = colorEndpoint.save(new ColorDto());

        // THEN
        checkResponse(response);
        verify(colorService, times(1)).save(any(ColorDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = colorEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(colorService, times(1)).delete(anyLong());
    }
}
