package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.services.RepairService;
import ru.shark.home.legomanager.services.dto.PartColorRenameDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/repair")
public class RepairEndpoint {

    private RepairService repairService;

    @POST
    @Path("/part/color/rename")
    public Response renamePartColor(PartColorRenameDto request) {
        return Response.ok(repairService.renamePartColor(request)).build();
    }

    @Autowired
    public void setRepairService(RepairService repairService) {
        this.repairService = repairService;
    }
}
