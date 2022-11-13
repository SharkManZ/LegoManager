package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.UserPartDto;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.dao.dto.request.UserPartListRequest;
import ru.shark.home.legomanager.datamanager.UserPartsDataManager;
import ru.shark.home.legomanager.services.dto.UserPartRequestDto;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserPartsServiceTest extends BaseServiceTest {
    private UserPartsService userPartsService;
    private UserPartsDataManager userPartsDataManager;

    @BeforeAll
    public void init() {
        userPartsService = new UserPartsService();
        userPartsDataManager = mock(UserPartsDataManager.class);
        userPartsService.setUserPartsDataManager(userPartsDataManager);
    }

    @BeforeEach
    public void initMethod() {
        reset(userPartsDataManager);
    }

    @Test
    public void getList() {
        // GIVEN
        when(userPartsDataManager.getList(any(UserPartListRequest.class), any(RequestCriteria.class)))
                .thenReturn(new PageableList<>(Arrays.asList(new UserPartListDto()), 1L));

        // WHEN
        BaseResponse list = userPartsService.getList(1L, new UserPartRequestDto());

        // THEN
        checkResponse(list);
        verify(userPartsDataManager, times(1)).getList(any(UserPartListRequest.class), any(RequestCriteria.class));
    }

    @Test
    public void save() {
        // GIVEN
        when(userPartsDataManager.save(any(UserPartDto.class))).thenReturn(new UserPartDto());

        // WHEN
        BaseResponse response = userPartsService.save(new UserPartDto());

        // THEN
        checkResponseWithBody(response);
        verify(userPartsDataManager, times(1)).save(any(UserPartDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = userPartsService.delete(1L);

        // THEN
        checkResponse(response);
        verify(userPartsDataManager, times(1)).deleteById(anyLong());
    }
}
