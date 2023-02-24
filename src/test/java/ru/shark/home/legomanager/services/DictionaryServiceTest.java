package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.datamanager.PartLoadSkipDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class DictionaryServiceTest extends BaseServiceTest {
    private PartLoadSkipDataManager partLoadSkipDataManager;
    private DictionaryService dictionaryService;

    @BeforeAll
    public void init() {
        partLoadSkipDataManager = mock(PartLoadSkipDataManager.class);
        dictionaryService = new DictionaryService();
        dictionaryService.setPartLoadSkipDataManager(partLoadSkipDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(partLoadSkipDataManager);
    }

    @Test
    public void getPartLoadSkipList() {
        // GIVEN
        when(partLoadSkipDataManager.getWithPagination(any(RequestCriteria.class))).thenReturn(new PageableList<>(
                new ArrayList<>(), 1L));

        // WHEN
        BaseResponse response = dictionaryService.getPartLoadSkipList(new PageRequest(0, 10));

        // THEN
        checkResponseWithBody(response);
        verify(partLoadSkipDataManager, times(1)).getWithPagination(any(RequestCriteria.class));
    }

    @Test
    public void savePartLoadSkip() {
        // GIVEN
        PartLoadSkipDto dto = new PartLoadSkipDto();
        when(partLoadSkipDataManager.save(any(PartLoadSkipDto.class))).thenReturn(new PartLoadSkipDto());

        // WHEN
        BaseResponse response = dictionaryService.savePartLoadSkip(dto);

        // THEN
        checkResponseWithBody(response);
        verify(partLoadSkipDataManager, times(1)).save(eq(dto));
    }

    @Test
    public void deletePartLoadSkip() {
        // WHEN
        BaseResponse response = dictionaryService.deletePartLoadSkip(1L);

        // THEN
        checkResponse(response);
        verify(partLoadSkipDataManager, times(1)).deleteById(eq(1L));
    }
}
