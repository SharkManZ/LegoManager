package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.legomanager.services.ExportService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

public class ExportEndpointTest extends BaseEndpointTest {
    private ExportEndpoint exportEndpoint;
    private ExportService exportService;

    @BeforeAll
    public void init() {
        exportService = mock(ExportService.class);
        exportEndpoint = new ExportEndpoint();
        exportEndpoint.setExportService(exportService);
    }

    @BeforeEach
    public void initMethod() {
        reset(exportService);
    }

    @Test
    public void exportAllData() {
        // GIVEN
        when(exportService.exportAllData()).thenReturn(mock(Response.class));

        // WHEN
        Response response = exportEndpoint.exportAllData();

        // THEN
        Assertions.assertNotNull(response);
        verify(exportService, times(1)).exportAllData();
    }
}
