package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.services.ColorService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/colors")
public class ColorEndpoint {

    private ColorService colorService;

    @POST
    @Path("/list")
    public Response getList(PageRequest request) {
        return Response.ok(colorService.getList(request)).build();
    }

    @POST
    @Path("/save")
    public Response save(ColorDto dto) {
        return Response.ok(colorService.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(colorService.delete(id)).build();
    }

    @Autowired
    public void setColorService(ColorService colorService) {
        this.colorService = colorService;
    }
}
