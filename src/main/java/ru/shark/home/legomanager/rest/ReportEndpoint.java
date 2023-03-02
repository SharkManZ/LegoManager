package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.services.TotalsService;
import ru.shark.home.legomanager.services.dto.TotalsRequestDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/report")
public class ReportEndpoint {

    private TotalsService totalsService;

    @POST
    @Path("/totals")
    public Response getTotals(TotalsRequestDto request) {
        return Response.ok(totalsService.getTotals(request)).build();
    }

    @Autowired
    public void setTotalsService(TotalsService totalsService) {
        this.totalsService = totalsService;
    }
}
