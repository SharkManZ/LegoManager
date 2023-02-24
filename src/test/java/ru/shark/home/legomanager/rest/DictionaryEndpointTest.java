package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.services.DictionaryService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

public class DictionaryEndpointTest extends BaseEndpointTest {
    private DictionaryService dictionaryService;
    private DictionaryEndpoint dictionaryEndpoint;

    @BeforeAll
    public void init() {
        dictionaryService = mock(DictionaryService.class);
        dictionaryEndpoint = new DictionaryEndpoint();
        dictionaryEndpoint.setDictionaryService(dictionaryService);
    }

    @BeforeEach
    public void initMethod() {
        reset(dictionaryService);
    }

    @Test
    public void getPartLoadSkipList() {
        // WHEN
        Response response = dictionaryEndpoint.getPartLoadSkipList(new PageRequest());

        // THEN
        checkResponse(response);
        verify(dictionaryService, times(1)).getPartLoadSkipList(any(PageRequest.class));
    }

    @Test
    public void savePartLoadSkipList() {
        // WHEN
        Response response = dictionaryEndpoint.savePartLoadSkipList(new PartLoadSkipDto());

        // THEN
        checkResponse(response);
        verify(dictionaryService, times(1)).savePartLoadSkip(any(PartLoadSkipDto.class));
    }

    @Test
    public void deletePartLoadSkip() {
        // WHEN
        Response response = dictionaryEndpoint.deletePartLoadSkip(1L);

        // THEN
        checkResponse(response);
        verify(dictionaryService, times(1)).deletePartLoadSkip(anyLong());
    }
}
