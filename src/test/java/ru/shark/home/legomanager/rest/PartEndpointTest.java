package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.services.ColorService;
import ru.shark.home.legomanager.services.PartColorService;
import ru.shark.home.legomanager.services.PartService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PartEndpointTest extends BaseEndpointTest {
    private PartEndpoint partEndpoint;
    private PartService partService;
    private PartColorService partColorService;
    private ColorService colorService;

    @BeforeAll
    public void init() {
        partService = mock(PartService.class);
        partColorService = mock(PartColorService.class);
        colorService = mock(ColorService.class);
        partEndpoint = new PartEndpoint();
        partEndpoint.setService(partService);
        partEndpoint.setPartColorService(partColorService);
        partEndpoint.setColorService(colorService);
    }

    @BeforeEach
    public void initMethod() {
        reset(partService, partColorService, colorService);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest pageRequest = new PageRequest(1, 10);

        // WHEN
        Response response = partEndpoint.getList(pageRequest);

        // THEN
        checkResponse(response);
        verify(partService, times(1)).getList(any(PageRequest.class));
    }

    @Test
    public void save() {
        // WHEN
        Response response = partEndpoint.save(new PartDto());

        // WHEN
        checkResponse(response);
        verify(partService, times(1)).save(any(PartDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = partEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(partService, times(1)).delete(eq(1L));
    }

    @Test
    public void getColorList() {
        // WHEN
        Response response = partEndpoint.getColorList(1L, new Search());

        // THEN
        checkResponse(response);
        verify(partColorService, times(1)).getListByPart(anyLong(), any(Search.class));
    }

    @Test
    public void getNotExistsColorsList() {
        // WHEN
        Response response = partEndpoint.getNotExistsColorsList(1L);

        // THEN
        checkResponse(response);
        verify(colorService, times(1)).getPartNotExistsColors(anyLong());
    }
}
