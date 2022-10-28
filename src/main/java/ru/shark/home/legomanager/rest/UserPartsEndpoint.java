package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.dto.UserPartDto;
import ru.shark.home.legomanager.services.UserPartsService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/userPart")
public class UserPartsEndpoint {

    private UserPartsService userPartsService;

    @POST
    @Path("/{userId}/list")
    public Response getList(@PathParam("userId") Long userId) {
        return Response.ok(userPartsService.getList(userId)).build();
    }

    @POST
    @Path("/save")
    public Response save(UserPartDto dto) {
        return Response.ok(userPartsService.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(userPartsService.delete(id)).build();
    }


    @Autowired
    public void setUserPartsService(UserPartsService userPartsService) {
        this.userPartsService = userPartsService;
    }
}
