package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.UserSetDto;
import ru.shark.home.legomanager.services.UserSetsService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/userSet")
public class UserSetsEndpoint {

    private UserSetsService userSetsService;

    @POST
    @Path("/{userId}/list")
    public Response getList(@PathParam("userId") Long userId, PageRequest request) {
        return Response.ok(userSetsService.getList(userId, request)).build();
    }

    @POST
    @Path("/save")
    public Response save(UserSetDto dto) {
        return Response.ok(userSetsService.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(userSetsService.delete(id)).build();
    }

    @Autowired
    public void setUserSetsService(UserSetsService userSetsService) {
        this.userSetsService = userSetsService;
    }
}
