package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.SetDto;
import ru.shark.home.legomanager.dao.dto.SetFullDto;
import ru.shark.home.legomanager.datamanager.SetDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class SetServiceTest extends BaseServiceTest {
    private SetService service;
    private SetDataManager setDataManager;

    @BeforeAll
    public void init() {
        setDataManager = mock(SetDataManager.class);
        service = new SetService();
        service.setSetDataManager(setDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(setDataManager);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest request = new PageRequest(0, 10);
        PageableList<SetFullDto> list = new PageableList<>(Arrays.asList(new SetFullDto()), 10L);
        when(setDataManager.getWithPagination(any(RequestCriteria.class))).thenReturn(list);

        // WHEN
        BaseResponse response = service.getList(request);

        // THEN
        checkPagingResponse(response);
        verify(setDataManager, times(1)).getWithPagination(any(RequestCriteria.class));
    }

    @Test
    public void save() {
        // WHEN
        BaseResponse response = service.save(new SetDto());

        // THEN
        checkResponse(response);
        verify(setDataManager, times(1)).save(any(SetDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = service.delete(1L);

        // THEN
        checkResponse(response);
        verify(setDataManager, times(1)).deleteById(eq(1L));
    }
}
