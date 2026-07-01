package resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Root discovery endpoint: GET /api/v1
 */
@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getDiscoveryInfo() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("version", "1.0");
        response.put("admin", "admin@mlops.com");

        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("workspaces", "/api/v1/workspaces");
        resources.put("models", "/api/v1/models");

        response.put("resources", resources);
        return response;
    }
    
    @GET
    @Path("/crash")
    public String crash() {
        throw new NullPointerException("Demo error");
    }
}
