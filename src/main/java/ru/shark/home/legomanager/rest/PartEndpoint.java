package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.services.PartColorService;
import ru.shark.home.legomanager.services.PartService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/parts")
public class PartEndpoint {

    private PartService service;
    private PartColorService partColorService;

    @POST
    @Path("/list")
    public Response getList(PageRequest request) {
        return Response.ok(service.getList(request)).build();
    }

    @POST
    @Path("/save")
    public Response save(PartDto dto) {
        return Response.ok(service.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(service.delete(id)).build();
    }

    @POST
    @Path("/{id}/color/list")
    public Response getColorList(@PathParam("id") Long id) {
        return Response.ok(partColorService.getListByPart(id)).build();
    }

    @Autowired
    public void setService(PartService service) {
        this.service = service;
    }

    @Autowired
    public void setPartColorService(PartColorService partColorService) {
        this.partColorService = partColorService;
    }
}
