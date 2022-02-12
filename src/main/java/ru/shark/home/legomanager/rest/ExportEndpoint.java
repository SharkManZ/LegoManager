package ru.shark.home.legomanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.services.ExportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Consumes("application/json")
@Path("/export")
public class ExportEndpoint {

    private ExportService exportService;

    @GET()
    @Path("/all")
    @Produces("application/octet-stream")
    public Response exportAllData() {
        return exportService.exportAllData();
    }

    @Autowired
    public void setExportService(ExportService exportService) {
        this.exportService = exportService;
    }
}
