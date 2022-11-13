package ru.shark.home.legomanager.services;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.dao.dto.load.RemoteSetPartsDto;
import ru.shark.home.legomanager.datamanager.PartColorDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static ru.shark.home.legomanager.services.LoadService.*;

public class LoadServiceTest extends BaseServiceTest {
    private LoadService loadService;
    private RemoteDataProvider remoteDataProvider;
    private PartColorDataManager partColorDataManager;

    @BeforeAll
    public void init() {
        partColorDataManager = mock(PartColorDataManager.class);
        remoteDataProvider = mock(RemoteDataProvider.class);
        loadService = new LoadService();
        loadService.setPartColorDataManager(partColorDataManager);
        loadService.setRemoteDataProvider(remoteDataProvider);
    }

    @BeforeEach
    public void initMethod() {
        clearInvocations(partColorDataManager);
    }

    @Test
    public void checkParts() throws IOException, URISyntaxException {
        // GIVEN
        String setNumber = "42082";
        String setData = getFileData("/remote/remote42082SetData.txt");
        String setPartsData = getFileData("/remote/remote42082PartsData.txt");
        when(remoteDataProvider.getDataFromUrl(eq(String.format(SOURCE_PORTAL + SET_ID_URL, setNumber)), anyString()))
                .thenReturn(setData);
        when(remoteDataProvider.getDataFromUrl(eq(String.format(SOURCE_PORTAL + PARTS_URL, 162427, setNumber)), anyString()))
                .thenReturn(setPartsData);
        when(partColorDataManager.findALl()).thenReturn(Arrays.asList(createPartColor("123", "123000"),
                createPartColor("234", "234000")));

        // WHEN
        BaseResponse response = loadService.checkParts("42082");

        // THEN
        checkResponseWithBody(response);
        List<RemoteSetPartsDto> body = (List<RemoteSetPartsDto>) response.getBody();
        Assertions.assertEquals(263, body.size());
        verify(partColorDataManager, times(1)).findALl();
        verify(remoteDataProvider, times(2)).getDataFromUrl(anyString(), anyString());
    }

    private String getFileData(String path) throws IOException, URISyntaxException {
        File fl = new File(this.getClass().getResource(path).toURI());
        return FileUtils.readFileToString(fl, StandardCharsets.UTF_8);
    }

    private PartColorDto createPartColor(String partNumber, String colorNumber) {
        PartColorDto dto = new PartColorDto();
        dto.setId(1L);
        dto.setNumber(colorNumber);
        dto.setPart(new PartDto());
        dto.getPart().setNumber(partNumber);
        dto.getPart().setName("part name " + partNumber);

        return dto;
    }
}
