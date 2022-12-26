package ru.shark.home.legomanager.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shark.home.legomanager.services.TotalsService;
import ru.shark.home.legomanager.services.dto.TotalsRequestDto;
import ru.shark.home.legomanager.util.BaseEndpointTest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

public class ReportEndpointTest extends BaseEndpointTest {
    private ReportEndpoint reportEndpoint;
    private TotalsService totalsService;

    @BeforeAll
    public void init() {
        totalsService = mock(TotalsService.class);
        reportEndpoint = new ReportEndpoint();
        reportEndpoint.setTotalsService(totalsService);
    }

    @BeforeEach
    public void initMethod() {
        reset(totalsService);
    }

    @Test
    public void getTotals() {
        // WHEN
        Response totals = reportEndpoint.getTotals(new TotalsRequestDto());

        // THEN
        checkResponse(totals);
        verify(totalsService, times(1)).getTotals(any(TotalsRequestDto.class));
    }
}
