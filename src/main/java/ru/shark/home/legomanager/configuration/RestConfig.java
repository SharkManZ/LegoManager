package ru.shark.home.legomanager.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import ru.shark.home.legomanager.rest.ColorEndpoint;
import ru.shark.home.legomanager.rest.DictionaryEndpoint;
import ru.shark.home.legomanager.rest.ExportEndpoint;
import ru.shark.home.legomanager.rest.LoadEndpoint;
import ru.shark.home.legomanager.rest.PartCategoryEndpoint;
import ru.shark.home.legomanager.rest.PartColorEndpoint;
import ru.shark.home.legomanager.rest.PartEndpoint;
import ru.shark.home.legomanager.rest.RepairEndpoint;
import ru.shark.home.legomanager.rest.ReportEndpoint;
import ru.shark.home.legomanager.rest.SeriesEndpoint;
import ru.shark.home.legomanager.rest.SetEndpoint;
import ru.shark.home.legomanager.rest.SetPartEndpoint;
import ru.shark.home.legomanager.rest.UserPartsEndpoint;
import ru.shark.home.legomanager.rest.UserSetsEndpoint;
import ru.shark.home.legomanager.rest.UsersEndpoint;

@Configuration
public class RestConfig extends ResourceConfig {
    public RestConfig() {
        register(SeriesEndpoint.class);
        register(SetEndpoint.class);
        register(ReportEndpoint.class);
        register(ColorEndpoint.class);
        register(PartCategoryEndpoint.class);
        register(PartEndpoint.class);
        register(PartColorEndpoint.class);
        register(SetPartEndpoint.class);
        register(ExportEndpoint.class);
        register(UsersEndpoint.class);
        register(UserSetsEndpoint.class);
        register(UserPartsEndpoint.class);
        register(LoadEndpoint.class);
        register(DictionaryEndpoint.class);
        register(RepairEndpoint.class);
    }
}
