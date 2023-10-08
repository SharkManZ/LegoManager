package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.services.LoadService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/load")
public class LoadEndpoint {

    private LoadService loadService;

    @GET
    @Path("/{setNumber}/checkParts")
    public Response checkParts(@PathParam("setNumber") String setNumber) {
        return Response.ok(loadService.checkParts(setNumber)).build();
    }

    @POST
    @Path("/{setNumber}/loadParts")
    public Response loadSetParts(@PathParam("setNumber") String setNumber) {
        return Response.ok(loadService.loadSetParts(setNumber)).build();
    }

    @POST
    @Path("/part/skip/list")
    public Response partLoadSkipList(PageRequest request) {
        return Response.ok(loadService.getPartLoadSkipList(request)).build();
    }

    @POST
    @Path("/part/skip/save")
    public Response partLoadSkipSave(PartLoadSkipDto dto) {
        return Response.ok(loadService.partLoadSkipSave(dto)).build();
    }

    @Autowired
    public void setLoadService(LoadService loadService) {
        this.loadService = loadService;
    }
}
