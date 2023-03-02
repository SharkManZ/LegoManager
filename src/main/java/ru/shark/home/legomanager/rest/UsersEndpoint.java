package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.UserDto;
import ru.shark.home.legomanager.services.UsersService;

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
@Path("/users")
public class UsersEndpoint {

    private UsersService usersService;

    @POST
    @Path("/list")
    public Response getList(PageRequest request) {
        return Response.ok(usersService.getList(request)).build();
    }

    @GET
    @Path("/list/all")
    public Response getAllList() {
        return Response.ok(usersService.getAllList()).build();
    }

    @POST
    @Path("/save")
    public Response save(UserDto dto) {
        return Response.ok(usersService.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(usersService.delete(id)).build();
    }

    @GET
    @Path("/{id}/setsSummary")
    public Response getSetsSummary(@PathParam("id") Long id) {
        return Response.ok(usersService.getUserSetsSummary(id)).build();
    }

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
}
