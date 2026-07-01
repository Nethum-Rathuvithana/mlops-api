package mapper;

import exception.LinkedWorkspaceNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class LinkedWorkspaceNotFoundMapper implements ExceptionMapper<LinkedWorkspaceNotFoundException> {

    @Override
    public Response toResponse(LinkedWorkspaceNotFoundException exception) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", "Referenced workspace does not exist");
        return Response.status(422)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
