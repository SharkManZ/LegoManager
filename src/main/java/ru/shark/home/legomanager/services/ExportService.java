package ru.shark.home.legomanager.services;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dto.FileDto;
import ru.shark.home.common.util.ZipUtils;
import ru.shark.home.legomanager.dao.service.ExportDao;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class ExportService {

    private ExportDao exportDao;

    public Response exportAllData() {
        List<FileDto> files = Arrays.asList(new FileDto("colors.json", exportDao.exportColors()),
                new FileDto("partCategories.json", exportDao.exportPartCategories()),
                new FileDto("series.json", exportDao.exportSeries()),
                new FileDto("users.json", exportDao.exportUsers()));
        byte[] bytes = ZipUtils.objectsListToZip(files);
        String dateStr = DateFormatUtils.format(new Date(), "dd.mm.yyyy");
        String fileName = "lego_data_export_" + dateStr + ".zip";
        return Response.ok(bytes, MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .header("content-disposition", "attachment; filename=\"" + fileName + "\"")
                .header("x-file-name", fileName)
                .type("application/zip")
                .build();
    }

    @Autowired
    public void setExportDao(ExportDao exportDao) {
        this.exportDao = exportDao;
    }
}
