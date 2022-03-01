package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.UserSetDto;
import ru.shark.home.legomanager.datamanager.UserSetsDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class UserSetsServiceTest extends BaseServiceTest {
    private UserSetsService userSetsService;
    private UserSetsDataManager userSetsDataManager;

    @BeforeAll
    public void init() {
        userSetsDataManager = mock(UserSetsDataManager.class);
        userSetsService = new UserSetsService();
        userSetsService.setUserSetsDataManager(userSetsDataManager);
    }

    @Test
    public void getList() {
        // GIVEN
        when(userSetsDataManager.getWithPagination(anyLong(), any(RequestCriteria.class)))
                .thenReturn(new PageableList<>(Arrays.asList(new UserSetDto()), 1L));
        // WHEN
        BaseResponse response = userSetsService.getList(1L, new PageRequest());

        // THEN
        checkResponse(response);
        verify(userSetsDataManager, times(1)).getWithPagination(anyLong(), any(RequestCriteria.class));
    }

    @Test
    public void save() {
        // GIVEN
        when(userSetsDataManager.save(any(UserSetDto.class))).thenReturn(new UserSetDto());

        // WHEN
        BaseResponse response = userSetsService.save(new UserSetDto());

        // THEN
        checkResponse(response);
        verify(userSetsDataManager, times(1)).save(any(UserSetDto.class));
    }

    @Test
    public void delete() {
        // WHEN
        BaseResponse response = userSetsService.delete(1L);

        // THEN
        checkResponse(response);
        verify(userSetsDataManager, times(1)).deleteById(anyLong());
    }
}
