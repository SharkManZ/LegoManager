package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.services.PartColorService;
import ru.shark.home.legomanager.services.dto.SearchDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/part/colors")
public class PartColorEndpoint {
    private PartColorService partColorService;

    @POST
    @Path("/save")
    public Response save(PartColorDto dto) {
        return Response.ok(partColorService.save(dto)).build();
    }

    @POST
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(partColorService.delete(id)).build();
    }

    @POST
    @Path("/search")
    public Response search(SearchDto dto) {
        return Response.ok(partColorService.search(dto)).build();
    }

    @Autowired
    public void setPartColorService(PartColorService partColorService) {
        this.partColorService = partColorService;
    }
}
