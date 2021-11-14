package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.dao.dto.SeriesFullDto;
import ru.shark.home.legomanager.datamanager.SeriesDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SeriesServiceTest extends BaseServiceTest {
    private SeriesService seriesService;
    private SeriesDataManager seriesDataManager;

    @BeforeAll
    public void init() {
        seriesDataManager = mock(SeriesDataManager.class);
        seriesService = new SeriesService();
        seriesService.setSeriesDataManager(seriesDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(seriesDataManager);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest request = new PageRequest(0, 10);
        PageableList<SeriesFullDto> pageList = new PageableList<>(Arrays.asList(new SeriesFullDto()), 1L);
        when(seriesDataManager.getWithPagination(any(RequestCriteria.class))).thenReturn(pageList);

        // WHEN
        BaseResponse response = seriesService.getList(request);

        // THEN
        checkPagingResponse(response);
        verify(seriesDataManager, times(1)).getWithPagination(any(RequestCriteria.class));
    }

    @Test
    public void save() {
        // GIVEN
        when(seriesDataManager.save(any(SeriesDto.class))).thenReturn(new SeriesDto());
        // WHEN
        BaseResponse response = seriesService.save(new SeriesDto());

        // THEN
        checkResponseWithBody(response);
        verify(seriesDataManager, times(1)).save(any(SeriesDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = seriesService.delete(1L);

        // THEN
        checkResponse(response);
        verify(seriesDataManager, times(1)).deleteById(anyLong());
    }
}
