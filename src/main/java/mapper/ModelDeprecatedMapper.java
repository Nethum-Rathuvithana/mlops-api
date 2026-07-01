package mapper;

import exception.ModelDeprecatedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class ModelDeprecatedMapper implements ExceptionMapper<ModelDeprecatedException> {

    @Override
    public Response toResponse(ModelDeprecatedException exception) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", "Model is deprecated");
        return Response.status(403)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
