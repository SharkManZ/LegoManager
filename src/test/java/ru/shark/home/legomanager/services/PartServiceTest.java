package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.dao.dto.PartFullDto;
import ru.shark.home.legomanager.datamanager.PartDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class PartServiceTest extends BaseServiceTest {
    private PartService partService;
    private PartDataManager partDataManager;

    @BeforeAll
    public void init() {
        partDataManager = mock(PartDataManager.class);
        partService = new PartService();
        partService.setPartDataManager(partDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(partDataManager);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest request = new PageRequest(0, 10);
        PageableList<PartFullDto> list = new PageableList<>(Arrays.asList(new PartFullDto()), 10L);
        when(partDataManager.getWithPagination(any(RequestCriteria.class))).thenReturn(list);

        // WHEN
        BaseResponse response = partService.getList(request);

        // THEN
        checkPagingResponse(response);
        verify(partDataManager, times(1)).getWithPagination(any(RequestCriteria.class));
    }

    @Test
    public void save() {
        // WHEN
        BaseResponse response = partService.save(new PartDto());

        // THEN
        checkResponse(response);
        verify(partDataManager, times(1)).savePart(any(PartDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = partService.delete(1L);

        // THEN
        checkResponse(response);
        verify(partDataManager, times(1)).deleteById(eq(1L));
    }
}
