package mapper;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Catch-all exception mapper. Any exception that does not have a more
 * specific ExceptionMapper registered (WorkspaceNotEmptyException,
 * LinkedWorkspaceNotFoundException, ModelDeprecatedException) will be
 * handled here and converted into a generic HTTP 500 response.
 *
 * JAX-RS already routes built-in framework exceptions (e.g. 404 for an
 * unmatched route, 405 for an unsupported method) through their own
 * WebApplicationException responses, so those are passed through
 * unchanged here instead of being overwritten with a 500.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", "Internal server error");
        return Response.status(500)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
