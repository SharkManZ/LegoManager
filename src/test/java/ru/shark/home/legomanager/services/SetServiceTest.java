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
import ru.shark.home.legomanager.datamanager.ColorDataManager;
import ru.shark.home.legomanager.datamanager.PartCategoryDataManager;
import ru.shark.home.legomanager.datamanager.SetDataManager;
import ru.shark.home.legomanager.services.dto.SearchDto;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class SetServiceTest extends BaseServiceTest {
    private SetService service;
    private SetDataManager setDataManager;
    private ColorDataManager colorDataManager;
    private PartCategoryDataManager partCategoryDataManager;

    @BeforeAll
    public void init() {
        setDataManager = mock(SetDataManager.class);
        colorDataManager = mock(ColorDataManager.class);
        partCategoryDataManager = mock(PartCategoryDataManager.class);
        service = new SetService();
        service.setSetDataManager(setDataManager);
        service.setColorDataManager(colorDataManager);
        service.setPartCategoryDataManager(partCategoryDataManager);
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
    public void getListWithSeriesId() {
        // GIVEN
        PageRequest request = new PageRequest(0, 10);
        PageableList<SetFullDto> list = new PageableList<>(Arrays.asList(new SetFullDto()), 10L);
        when(setDataManager.getWithPagination(any(RequestCriteria.class), anyLong())).thenReturn(list);

        // WHEN
        BaseResponse response = service.getList(request, 1L);

        // THEN
        checkPagingResponse(response);
        verify(setDataManager, times(1)).getWithPagination(any(RequestCriteria.class), anyLong());
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

    @Test
    public void getSummary() {
        // WHEN
        BaseResponse response = service.getSummary(1L);

        // THEN
        checkResponse(response);
        verify(setDataManager, times(1)).getSummary(eq(1L));
    }

    @Test
    public void getSetColors() {
        // WHEN
        BaseResponse response = service.getSetColors(1L);

        // THEN
        checkResponse(response);
        verify(colorDataManager, times(1)).getListBySetId(eq(1L));
    }

    @Test
    public void getSetPartCategories() {
        // WHEN
        BaseResponse response = service.getSetPartCategories(1L);

        // THEN
        checkResponse(response);
        verify(partCategoryDataManager, times(1)).getListBySetId(eq(1L));
    }

    @Test
    public void search() {
        // GIVEN
        when(setDataManager.search(any(SearchDto.class))).thenReturn(new SetDto());

        // WHEN
        BaseResponse response = service.search(new SearchDto());

        // THEN
        checkResponse(response);
        verify(setDataManager, times(1)).search(any(SearchDto.class));
    }
}
