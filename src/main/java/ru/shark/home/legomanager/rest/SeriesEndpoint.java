package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.services.SeriesService;
import ru.shark.home.legomanager.services.SetService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/series")
public class SeriesEndpoint {

    private SeriesService seriesService;
    private SetService setService;

    @POST
    @Path("/list")
    public Response getList(PageRequest request) {
        return Response.ok(seriesService.getList(request)).build();
    }

    @POST
    @Path("/list/all")
    public Response getAllList() {
        return Response.ok(seriesService.getAllList()).build();
    }

    @POST
    @Path("/{id}/sets/list")
    public Response getSetsList(@PathParam("id") Long id, PageRequest request) {
        return Response.ok(setService.getList(request, id)).build();
    }

    @POST
    @Path("/save")
    public Response save(SeriesDto dto) {
        return Response.ok(seriesService.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(seriesService.delete(id)).build();
    }

    @Autowired
    public void setSeriesService(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @Autowired
    public void setSetService(SetService setService) {
        this.setService = setService;
    }
}
