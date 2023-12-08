package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.legomanager.dao.service.ExportDao;
import ru.shark.home.legomanager.util.BaseServiceTest;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ExportServiceTest extends BaseServiceTest {
    private ExportService exportService;
    private ExportDao exportDao;

    @BeforeAll
    public void init() {
        exportDao = mock(ExportDao.class);
        exportService = new ExportService();
        exportService.setExportDao(exportDao);
    }

    @BeforeEach
    public void initMethod() {
        reset(exportDao);
    }

    @Test
    public void exportAllData() {
        // GIVEN
        when(exportDao.exportColors()).thenReturn(new ArrayList<>());
        when(exportDao.exportPartCategories()).thenReturn(new ArrayList<>());
        when(exportDao.exportSeries()).thenReturn(new ArrayList<>());
        when(exportDao.exportUsers()).thenReturn(new ArrayList<>());
        when(exportDao.exportPartLoadSkip()).thenReturn(new ArrayList<>());
        when(exportDao.exportPartLoadComparison()).thenReturn(new ArrayList<>());

        // WHEN
        Response response = exportService.exportAllData();

        // THEN
        Assertions.assertNotNull(response);
        verify(exportDao, times(1)).exportColors();
        verify(exportDao, times(1)).exportPartCategories();
        verify(exportDao, times(1)).exportSeries();
        verify(exportDao, times(1)).exportUsers();
        verify(exportDao, times(1)).exportPartLoadSkip();
        verify(exportDao, times(1)).exportPartLoadComparison();
    }
}
