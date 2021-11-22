package ru.shark.home.legomanager.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import ru.shark.home.legomanager.rest.ColorEndpoint;
import ru.shark.home.legomanager.rest.ReportEndpoint;
import ru.shark.home.legomanager.rest.SeriesEndpoint;
import ru.shark.home.legomanager.rest.SetEndpoint;

@Configuration
public class RestConfig extends ResourceConfig {
    public RestConfig() {
        register(SeriesEndpoint.class);
        register(SetEndpoint.class);
        register(ReportEndpoint.class);
        register(ColorEndpoint.class);
    }
}
