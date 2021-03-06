package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.SetPartDto;
import ru.shark.home.legomanager.datamanager.SetPartDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class SetPartServiceTest extends BaseServiceTest {
    private SetPartService setPartService;
    private SetPartDataManager setPartDataManager;

    @BeforeAll
    public void init() {
        setPartDataManager = mock(SetPartDataManager.class);
        setPartService = new SetPartService();
        setPartService.setSetPartDataManager(setPartDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(setPartDataManager);
    }

    @Test
    public void getListBySetId() {
        // GIVEN
        when(setPartDataManager.getPartsBySetId(anyLong(), any(RequestCriteria.class)))
                .thenReturn(new PageableList<>(Collections.emptyList(), 1L));

        // WHEN
        BaseResponse response = setPartService.getListBySetId(1L, new PageRequest());

        // THEN
        checkResponseWithBody(response);
        verify(setPartDataManager, times(1)).getPartsBySetId(anyLong(), any(RequestCriteria.class));
    }

    @Test
    public void save() {
        // GIVEN
        when(setPartDataManager.save(any(SetPartDto.class))).thenReturn(new SetPartDto());

        // WHEN
        BaseResponse response = setPartService.save(new SetPartDto());

        // THEN
        checkResponseWithBody(response);
        verify(setPartDataManager, times(1)).save(any(SetPartDto.class));
    }

    @Test
    public void deleteById() {
        // WHEN
        BaseResponse response = setPartService.deleteById(1L);

        // THEN
        checkResponse(response);
        verify(setPartDataManager, times(1)).deleteById(anyLong());
    }
}
