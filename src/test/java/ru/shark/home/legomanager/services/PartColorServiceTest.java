package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.datamanager.PartColorDataManager;
import ru.shark.home.legomanager.services.dto.PartColorSearchDto;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class PartColorServiceTest extends BaseServiceTest {
    private PartColorService partColorService;
    private PartColorDataManager partColorDataManager;

    @BeforeAll
    public void init() {
        partColorDataManager = mock(PartColorDataManager.class);
        partColorService = new PartColorService();
        partColorService.setPartColorDataManager(partColorDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(partColorDataManager);
    }

    @Test
    public void getListByPart() {
        // GIVEN
        when(partColorDataManager.getPartColorListByPartId(anyLong())).thenReturn(new ArrayList<>());

        // WHEN
        BaseResponse response = partColorService.getListByPart(1L);

        // THEN
        checkResponseWithBody(response);
        verify(partColorDataManager, times(1)).getPartColorListByPartId(anyLong());
    }

    @Test
    public void save() {
        // GIVEN
        when(partColorDataManager.save(any(PartColorDto.class))).thenReturn(new PartColorDto());

        // WHEN
        BaseResponse response = partColorService.save(new PartColorDto());

        // THEN
        checkResponseWithBody(response);
        verify(partColorDataManager, times(1)).save(any(PartColorDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = partColorService.delete(1L);

        // THEN
        checkResponse(response);
        verify(partColorDataManager, times(1)).deleteById(anyLong());
    }

    @Test
    public void search() {
        // GIVEN
        when(partColorDataManager.search(any(PartColorSearchDto.class))).thenReturn(new PartColorDto());

        // WHEN
        BaseResponse response = partColorService.search(new PartColorSearchDto());

        // THEN
        checkResponseWithBody(response);
        verify(partColorDataManager, times(1)).search(any(PartColorSearchDto.class));
    }
}
