import config.ApiApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

/**
 * Application entry point. Starts an embedded Grizzly HTTP server
 * hosting the Jersey JAX-RS application.
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) throws IOException {
        ResourceConfig config = new ApiApplication();
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

        LOGGER.info("MLOps Workspace API started at " + BASE_URI + "api/v1");
        LOGGER.info("Press CTRL+C to stop the server");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            server.shutdownNow();
        }
    }
}
