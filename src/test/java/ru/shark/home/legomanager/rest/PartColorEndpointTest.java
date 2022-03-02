package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.services.PartColorService;
import ru.shark.home.legomanager.services.dto.SearchDto;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

public class PartColorEndpointTest extends BaseEndpointTest {
    private PartColorEndpoint partColorEndpoint;
    private PartColorService partColorService;

    @BeforeAll
    public void init() {
        partColorService = mock(PartColorService.class);
        partColorEndpoint = new PartColorEndpoint();
        partColorEndpoint.setPartColorService(partColorService);
    }

    @BeforeEach
    public void initMethod() {
        reset(partColorService);
    }

    @Test
    public void save() {
        // GIVEN
        when(partColorService.save(any(PartColorDto.class))).thenReturn(new BaseResponse());

        // WHEN
        Response response = partColorEndpoint.save(new PartColorDto());

        // THEN
        checkResponse(response);
        verify(partColorService, times(1)).save(any(PartColorDto.class));
    }

    @Test
    public void delete() {
        // GIVEN
        when(partColorService.delete(anyLong())).thenReturn(new BaseResponse());

        // WHEN
        Response response = partColorEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(partColorService, times(1)).delete(anyLong());
    }

    @Test
    public void search() {
        // GIVEN
        when(partColorService.search(any(SearchDto.class))).thenReturn(new BaseResponse());

        // WHEN
        Response response = partColorEndpoint.search(new SearchDto());

        // THEN
        checkResponse(response);
        verify(partColorService, times(1)).search(any(SearchDto.class));
    }
}
