package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.UserPartDto;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.datamanager.UserPartsDataManager;
import ru.shark.home.legomanager.util.BaseServiceTest;

import java.util.Collections;

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
        when(userPartsDataManager.getList(anyLong())).thenReturn(Collections.singletonList(new UserPartListDto()));

        // WHEN
        BaseResponse list = userPartsService.getList(1L);

        // THEN
        checkResponse(list);
        verify(userPartsDataManager, times(1)).getList(anyLong());
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
