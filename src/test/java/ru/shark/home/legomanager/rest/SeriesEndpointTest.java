package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.services.SeriesService;
import ru.shark.home.legomanager.services.SetService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SeriesEndpointTest extends BaseEndpointTest {
    private SeriesEndpoint seriesEndpoint;
    private SeriesService seriesService;
    private SetService setService;

    @BeforeAll
    public void init() {
        seriesService = mock(SeriesService.class);
        setService = mock(SetService.class);
        seriesEndpoint = new SeriesEndpoint();
        seriesEndpoint.setSeriesService(seriesService);
        seriesEndpoint.setSetService(setService);
    }

    @BeforeEach
    public void initMethod() {
        reset(seriesService, setService);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest pageRequest = new PageRequest(0, 10);

        // WHEN
        Response response = seriesEndpoint.getList(pageRequest);

        // THEN
        checkResponse(response);
        verify(seriesService, times(1)).getList(any(PageRequest.class));
    }

    @Test
    public void getAllList() {
        // WHEN
        Response allList = seriesEndpoint.getAllList();

        // THEN
        checkResponse(allList);
        verify(seriesService, times(1)).getAllList();
    }

    @Test
    public void getSetsList() {
        // GIVEN
        PageRequest pageRequest = new PageRequest(0, 10);

        // WHEN
        Response allList = seriesEndpoint.getSetsList(1L, pageRequest);

        // THEN
        checkResponse(allList);
        verify(setService, times(1)).getList(any(PageRequest.class), anyLong());
    }

    @Test
    public void save() {
        // WHEN
        Response response = seriesEndpoint.save(new SeriesDto());

        // THEN
        checkResponse(response);
        verify(seriesService, times(1)).save(any(SeriesDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = seriesEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(seriesService, times(1)).delete(anyLong());
    }
}
