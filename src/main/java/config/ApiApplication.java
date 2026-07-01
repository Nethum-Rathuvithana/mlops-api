package config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * JAX-RS application configuration. Registers the base API path and
 * scans the resource/mapper/filter packages for providers.
 */
@ApplicationPath("/api/v1")
public class ApiApplication extends ResourceConfig {

    public ApiApplication() {
        packages("resource", "mapper", "filter");
        register(JacksonFeature.class);
    }
}
