package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.services.TotalsService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/report")
public class ReportEndpoint {

    private TotalsService totalsService;

    @GET
    @Path("/totals")
    public Response getTotals() {
        return Response.ok(totalsService.getTotals()).build();
    }

    @Autowired
    public void setTotalsService(TotalsService totalsService) {
        this.totalsService = totalsService;
    }
}
