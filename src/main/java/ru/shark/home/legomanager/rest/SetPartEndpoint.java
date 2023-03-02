package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.dto.SetPartDto;
import ru.shark.home.legomanager.services.SetPartService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/set/parts")
public class SetPartEndpoint {

    private SetPartService setPartService;

    @POST
    @Path("/save")
    public Response save(SetPartDto dto) {
        return Response.ok(setPartService.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(setPartService.deleteById(id)).build();
    }

    @Autowired
    public void setSetPartService(SetPartService setPartService) {
        this.setPartService = setPartService;
    }
}
