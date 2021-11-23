package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.services.PartService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PartEndpointTest extends BaseEndpointTest {
    private PartEndpoint partEndpoint;
    private PartService partService;

    @BeforeAll
    public void init() {
        partService = mock(PartService.class);
        partEndpoint = new PartEndpoint();
        partEndpoint.setService(partService);
    }

    @BeforeEach
    public void initMethod() {
        reset(partService);
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

        // WHEN
        checkResponse(response);
        verify(partService, times(1)).delete(eq(1L));
    }
}
