package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.PartCategoryDto;
import ru.shark.home.legomanager.services.PartCategoryService;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartCategoryEndpointTest extends BaseEndpointTest {
    private PartCategoryEndpoint partCategoryEndpoint;
    private PartCategoryService partCategoryService;

    @BeforeAll
    public void init() {
        partCategoryService = mock(PartCategoryService.class);
        partCategoryEndpoint = new PartCategoryEndpoint();
        partCategoryEndpoint.setPartCategoryService(partCategoryService);
    }

    @BeforeEach
    public void initMethod() {
        reset(partCategoryService);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest pageRequest = new PageRequest(0, 10);

        // WHEN
        Response response = partCategoryEndpoint.getList(pageRequest);

        // THEN
        checkResponse(response);
        verify(partCategoryService, times(1)).getList(any(PageRequest.class));
    }

    @Test
    public void getAllList() {
        // WHEN
        Response allList = partCategoryEndpoint.getAllList();

        // THEN
        checkResponse(allList);
        verify(partCategoryService, times(1)).getAllList();
    }

    @Test
    public void save() {
        // WHEN
        Response response = partCategoryEndpoint.save(new PartCategoryDto(1L, "name"));

        // THEN
        checkResponse(response);
        verify(partCategoryService, times(1)).save(any(PartCategoryDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        Response response = partCategoryEndpoint.delete(1L);

        // THEN
        checkResponse(response);
        verify(partCategoryService, times(1)).delete(anyLong());
    }
}
