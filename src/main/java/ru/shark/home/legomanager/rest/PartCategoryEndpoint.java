package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.PartCategoryDto;
import ru.shark.home.legomanager.services.PartCategoryService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/part/category")
public class PartCategoryEndpoint {

    private PartCategoryService partCategoryService;

    @POST
    @Path("/list")
    public Response getList(PageRequest request) {
        return Response.ok(partCategoryService.getList(request)).build();
    }

    @POST
    @Path("/list/all")
    public Response getAllList() {
        return Response.ok(partCategoryService.getAllList()).build();
    }

    @POST
    @Path("/save")
    public Response save(PartCategoryDto dto) {
        return Response.ok(partCategoryService.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(partCategoryService.delete(id)).build();
    }

    @Autowired
    public void setPartCategoryService(PartCategoryService partCategoryService) {
        this.partCategoryService = partCategoryService;
    }
}
