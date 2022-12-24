package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartCategoryDto;
import ru.shark.home.legomanager.datamanager.PartCategoryDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartCategoryServiceTest extends BaseServiceTest {
    private PartCategoryService partCategoryService;
    private PartCategoryDataManager partCategoryDataManager;

    @BeforeAll
    public void init() {
        partCategoryDataManager = mock(PartCategoryDataManager.class);
        partCategoryService = new PartCategoryService();
        partCategoryService.setPartCategoryDataManager(partCategoryDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(partCategoryDataManager);
    }

    @Test
    public void getList() {
        // GIVEN
        PageRequest request = new PageRequest(0, 10);
        PageableList<PartCategoryDto> pageList = new PageableList<>(Arrays.asList(new PartCategoryDto(1L, "name")), 1L);
        when(partCategoryDataManager.getWithPagination(any(RequestCriteria.class))).thenReturn(pageList);

        // WHEN
        BaseResponse response = partCategoryService.getList(request);

        // THEN
        checkPagingResponse(response);
        verify(partCategoryDataManager, times(1)).getWithPagination(any(RequestCriteria.class));
    }

    @Test
    public void getAllList() {
        // GIVEN
        when(partCategoryDataManager.getAllCategories()).thenReturn(new ArrayList<>());

        // WHEN
        BaseResponse allList = partCategoryService.getAllList();

        // THEN
        checkResponseWithBody(allList);
        verify(partCategoryDataManager, times(1)).getAllCategories();
    }

    @Test
    public void save() {
        // GIVEN
        when(partCategoryDataManager.save(any(PartCategoryDto.class))).thenReturn(new PartCategoryDto(1L, "name"));
        // WHEN
        BaseResponse response = partCategoryService.save(new PartCategoryDto(1L, "name"));

        // THEN
        checkResponseWithBody(response);
        verify(partCategoryDataManager, times(1)).save(any(PartCategoryDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = partCategoryService.delete(1L);

        // THEN
        checkResponse(response);
        verify(partCategoryDataManager, times(1)).deleteById(anyLong());
    }
}
