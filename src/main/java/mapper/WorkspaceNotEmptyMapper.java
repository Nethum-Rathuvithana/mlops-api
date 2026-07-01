package mapper;

import exception.WorkspaceNotEmptyException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class WorkspaceNotEmptyMapper implements ExceptionMapper<WorkspaceNotEmptyException> {

    @Override
    public Response toResponse(WorkspaceNotEmptyException exception) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", "Workspace contains models");
        return Response.status(409)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
