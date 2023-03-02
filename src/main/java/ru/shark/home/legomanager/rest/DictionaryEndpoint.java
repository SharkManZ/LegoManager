package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.services.DictionaryService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/dictionary")
public class DictionaryEndpoint {

    private DictionaryService dictionaryService;

    @POST
    @Path("/part/load/skip/list")
    public Response getPartLoadSkipList(PageRequest request) {
        return Response.ok(dictionaryService.getPartLoadSkipList(request)).build();
    }

    @POST
    @Path("/part/load/skip/save")
    public Response savePartLoadSkipList(PartLoadSkipDto dto) {
        return Response.ok(dictionaryService.savePartLoadSkip(dto)).build();
    }

    @POST
    @Path("/part/load/skip/{id}/delete")
    public Response deletePartLoadSkip(@PathParam("id") Long id) {
        return Response.ok(dictionaryService.deletePartLoadSkip(id)).build();
    }

    @Autowired
    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
}
