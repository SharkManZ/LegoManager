package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.legomanager.dao.dto.SetPartDto;
import ru.shark.home.legomanager.services.SetPartService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

public class SetPartEndpointTest extends BaseEndpointTest {
    private SetPartEndpoint setPartEndpoint;
    private SetPartService setPartService;

    @BeforeAll
    public void init() {
        setPartService = mock(SetPartService.class);
        setPartEndpoint = new SetPartEndpoint();
        setPartEndpoint.setSetPartService(setPartService);
    }

    @BeforeEach
    public void initMethod() {
        reset(setPartService);
    }

    @Test
    public void save() {
        // WHEN
        Response response = setPartEndpoint.save(new SetPartDto());

        // THEN
        checkResponse(response);
        verify(setPartService, timeout(1)).save(any(SetPartDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = setPartEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(setPartService, timeout(1)).deleteById(anyLong());
    }
}
