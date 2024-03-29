package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.SetDto;
import ru.shark.home.legomanager.services.SetPartService;
import ru.shark.home.legomanager.services.SetService;
import ru.shark.home.legomanager.services.dto.SearchDto;

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
@Path("/sets")
public class SetEndpoint {

    private SetService service;
    private SetPartService setPartService;

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

    @POST
    @Path("{id}/part/list")
    public Response getPartsList(@PathParam("id") Long id, PageRequest request) {
        return Response.ok(setPartService.getListBySetId(id, request)).build();
    }

    @GET
    @Path("{id}/summary")
    public Response getSummary(@PathParam("id") Long id) {
        return Response.ok(service.getSummary(id)).build();
    }

    @GET
    @Path("{id}/color/list")
    public Response getColors(@PathParam("id") Long id) {
        return Response.ok(service.getSetColors(id)).build();
    }

    @GET
    @Path("{id}/partCategories/list")
    public Response getPartCategories(@PathParam("id") Long id) {
        return Response.ok(service.getSetPartCategories(id)).build();
    }

    @POST
    @Path("/search")
    public Response search(SearchDto dto) {
        return Response.ok(service.search(dto)).build();
    }

    @Autowired
    public void setService(SetService service) {
        this.service = service;
    }

    @Autowired
    public void setSetPartService(SetPartService setPartService) {
        this.setPartService = setPartService;
    }
}
