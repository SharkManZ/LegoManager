package ru.shark.home.legomanager.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import ru.shark.home.legomanager.rest.*;

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
    }
}
