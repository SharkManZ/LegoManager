package ru.shark.home.legomanager.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.TotalDto;
import ru.shark.home.legomanager.dao.service.TotalsDao;
import ru.shark.home.legomanager.services.dto.TotalsDto;
import ru.shark.home.legomanager.util.BaseServiceTest;

import static org.mockito.Mockito.*;

public class TotalsServiceTest extends BaseServiceTest {
    private TotalsService totalsService;
    private TotalsDao totalsDao;

    @BeforeAll
    public void init() {
        totalsDao = mock(TotalsDao.class);
        totalsService = new TotalsService();
        totalsService.setTotalsDao(totalsDao);
    }

    @BeforeEach
    public void initMethod() {
        reset(totalsDao);
    }

    @Test
    public void getTotals() {
        // GIVEN
        when(totalsDao.getSeriesTotal()).thenReturn(new TotalDto());
        when(totalsDao.getSetsTotal()).thenReturn(new TotalDto());
        when(totalsDao.getPartsTotal()).thenReturn(new TotalDto());

        // WHEN
        BaseResponse response = totalsService.getTotals();

        // THEN
        checkResponse(response);
        TotalsDto body = (TotalsDto) response.getBody();
        Assertions.assertNotNull(body.getSeries());
        Assertions.assertNotNull(body.getSets());
        Assertions.assertNotNull(body.getDiffParts());
        Assertions.assertNotNull(body.getParts());
        verify(totalsDao, times(1)).getSeriesTotal();
        verify(totalsDao, times(1)).getSetsTotal();
        verify(totalsDao, times(1)).getPartsTotal();
    }
}
