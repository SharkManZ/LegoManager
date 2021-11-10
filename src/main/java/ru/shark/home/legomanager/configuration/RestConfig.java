package ru.shark.home.legomanager.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import ru.shark.home.legomanager.rest.ReportEndpoint;
import ru.shark.home.legomanager.rest.SeriesEndpoint;

@Configuration
public class RestConfig extends ResourceConfig {
    public RestConfig() {
        register(SeriesEndpoint.class);
        register(ReportEndpoint.class);
    }
}
