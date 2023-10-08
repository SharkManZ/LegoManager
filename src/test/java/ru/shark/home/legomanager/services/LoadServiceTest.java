package ru.shark.home.legomanager.services;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.dao.dto.load.RemoteSetPartsDto;
import ru.shark.home.legomanager.datamanager.PartColorDataManager;
import ru.shark.home.legomanager.datamanager.PartLoadSkipDataManager;
import ru.shark.home.legomanager.loader.SetDataLoader;
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
    private PartLoadSkipDataManager partLoadSkipDataManager;
    private SetDataLoader setDataLoader;

    @BeforeAll
    public void init() {
        partColorDataManager = mock(PartColorDataManager.class);
        remoteDataProvider = mock(RemoteDataProvider.class);
        setDataLoader = mock(SetDataLoader.class);
        partLoadSkipDataManager = mock(PartLoadSkipDataManager.class);
        loadService = new LoadService();
        loadService.setPartColorDataManager(partColorDataManager);
        loadService.setRemoteDataProvider(remoteDataProvider);
        loadService.setSetDataLoader(setDataLoader);
        loadService.setPartLoadSkipDataManager(partLoadSkipDataManager);
    }

    @BeforeEach
    public void initMethod() {
        clearInvocations(partColorDataManager, remoteDataProvider);
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
        when(setDataLoader.findMissingParts(anyList())).thenReturn(Arrays.asList(prepareRemoteDto(1L, "123", "123000", 1),
                prepareRemoteDto(2L, "234", "234000", 5)));

        // WHEN
        BaseResponse response = loadService.checkParts("42082");

        // THEN
        checkResponseWithBody(response);
        List<RemoteSetPartsDto> body = (List<RemoteSetPartsDto>) response.getBody();
        Assertions.assertEquals(2, body.size());
        verify(setDataLoader, times(1)).findMissingParts(anyList());
        verify(remoteDataProvider, times(2)).getDataFromUrl(anyString(), anyString());
    }

    @Test
    public void loadSetParts() throws IOException, URISyntaxException {
        // GIVEN
        String setNumber = "42082";
        String setData = getFileData("/remote/remote42082SetData.txt");
        String setPartsData = getFileData("/remote/remote42082PartsData.txt");
        when(remoteDataProvider.getDataFromUrl(eq(String.format(SOURCE_PORTAL + SET_ID_URL, setNumber)), anyString()))
                .thenReturn(setData);
        when(remoteDataProvider.getDataFromUrl(eq(String.format(SOURCE_PORTAL + PARTS_URL, 162427, setNumber)), anyString()))
                .thenReturn(setPartsData);

        // WHEN
        BaseResponse response = loadService.loadSetParts("42082");

        // THEN
        checkResponse(response);
        verify(remoteDataProvider, times(2)).getDataFromUrl(anyString(), anyString());
        verify(setDataLoader, times(1)).loadSetParts(eq("42082"), anyList());
    }

    @Test
    public void getPartLoadSkipList() {
        // GIVEN
        PageRequest pageRequest = new PageRequest(0, 10);
        when(partLoadSkipDataManager.getWithPagination(any(RequestCriteria.class))).thenReturn(new PageableList<>(List.of(new PartLoadSkipDto()), 1L));

        // WHEN
        BaseResponse baseResponse = loadService.getPartLoadSkipList(pageRequest);

        // THEN
        checkPagingResponse(baseResponse);
        verify(partLoadSkipDataManager, times(1)).getWithPagination(any(RequestCriteria.class));
    }

    @Test
    public void partLoadSkipSave() {
        // GIVEN
        when(partLoadSkipDataManager.save(any(PartLoadSkipDto.class))).thenReturn(new PartLoadSkipDto());

        // WHEN
        BaseResponse baseResponse = loadService.partLoadSkipSave(new PartLoadSkipDto());

        // THEN
        checkResponse(baseResponse);
        verify(partLoadSkipDataManager, times(1)).save(any(PartLoadSkipDto.class));
    }

    private String getFileData(String path) throws IOException, URISyntaxException {
        File fl = new File(this.getClass().getResource(path).toURI());
        return FileUtils.readFileToString(fl, StandardCharsets.UTF_8);
    }

    private RemoteSetPartsDto prepareRemoteDto(Long id, String number, String colorNumber, Integer count) {
        RemoteSetPartsDto dto = new RemoteSetPartsDto();
        dto.setId(id);
        dto.setNumber(number);
        dto.setColorNumber(colorNumber);
        dto.setCount(count);
        dto.setName(number + " title");
        return dto;
    }
}
