package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.services.SetService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @Autowired
    public void setService(SetService service) {
        this.service = service;
    }
}
