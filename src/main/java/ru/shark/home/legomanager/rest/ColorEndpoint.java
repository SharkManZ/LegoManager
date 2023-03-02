package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.services.ColorService;

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
@Path("/colors")
public class ColorEndpoint {

    private ColorService colorService;

    @POST
    @Path("/list")
    public Response getList(PageRequest request) {
        return Response.ok(colorService.getList(request)).build();
    }

    @GET
    @Path("/list/all")
    public Response getAllList() {
        return Response.ok(colorService.getAllList()).build();
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
