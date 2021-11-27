package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.datamanager.ColorDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ColorServiceTest extends BaseServiceTest {
    private ColorService colorService;
    private ColorDataManager colorDataManager;

    @BeforeAll
    public void init() {
        colorDataManager = mock(ColorDataManager.class);
        colorService = new ColorService();
        colorService.setColorDataManager(colorDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(colorDataManager);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest request = new PageRequest(0, 10);
        PageableList<ColorDto> pageList = new PageableList<>(Arrays.asList(new ColorDto()), 1L);
        when(colorDataManager.getWithPagination(any(RequestCriteria.class))).thenReturn(pageList);

        // WHEN
        BaseResponse response = colorService.getList(request);

        // THEN
        checkPagingResponse(response);
        verify(colorDataManager, times(1)).getWithPagination(any(RequestCriteria.class));
    }

    @Test
    public void getAllList() {
        // GIVEN
        when(colorDataManager.getAllColors()).thenReturn(new ArrayList<>());

        // WHEN
        BaseResponse allList = colorService.getAllList();

        // THEN
        checkResponse(allList);
        verify(colorDataManager, times(1)).getAllColors();
    }

    @Test
    public void save() {
        // GIVEN
        when(colorDataManager.save(any(ColorDto.class))).thenReturn(new ColorDto());
        // WHEN
        BaseResponse response = colorService.save(new ColorDto());

        // THEN
        checkResponseWithBody(response);
        verify(colorDataManager, times(1)).save(any(ColorDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = colorService.delete(1L);

        // THEN
        checkResponse(response);
        verify(colorDataManager, times(1)).deleteById(anyLong());
    }
}
