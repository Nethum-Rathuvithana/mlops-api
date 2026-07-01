package filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logs the HTTP method and request URI for every incoming request, and
 * the HTTP method, request URI, and response status code for every
 * outgoing response.
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.log(Level.INFO, "Incoming request -> {0} {1}",
                new Object[]{requestContext.getMethod(), requestContext.getUriInfo().getRequestUri()});
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LOGGER.log(Level.INFO, "Outgoing response -> {0} {1} - status {2}",
                new Object[]{requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(), responseContext.getStatus()});
    }
}
