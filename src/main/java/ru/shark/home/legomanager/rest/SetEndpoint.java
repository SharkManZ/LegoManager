package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.SetDto;
import ru.shark.home.legomanager.services.SetService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/sets")
public class SetEndpoint {

    private SetService service;

    @POST
    @Path("/list")
    public Response getList(PageRequest request) {
        return Response.ok(service.getList(request)).build();
    }

    @POST
    @Path("/save")
    public Response save(SetDto dto) {
        return Response.ok(service.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(service.delete(id)).build();
    }

    @Autowired
    public void setService(SetService service) {
        this.service = service;
    }
}
