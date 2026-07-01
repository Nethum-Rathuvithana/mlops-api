package resource;

import exception.WorkspaceNotEmptyException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.MLWorkspace;
import storage.DataStore;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/workspaces")
public class WorkspaceResource {

    private final DataStore store = DataStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MLWorkspace> getAllWorkspaces() {
        return new ArrayList<>(store.getWorkspaces().values());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createWorkspace(MLWorkspace workspace) {
        String id = UUID.randomUUID().toString();
        workspace.setId(id);
        if (workspace.getModelIds() == null) {
            workspace.setModelIds(new ArrayList<>());
        }
        store.getWorkspaces().put(id, workspace);
        return Response.status(Response.Status.CREATED).entity(workspace).build();
    }

    @GET
    @Path("/{workspaceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkspace(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace workspace = store.getWorkspaces().get(workspaceId);
        if (workspace == null) {
            return notFound();
        }
        return Response.ok(workspace).build();
    }

    @DELETE
    @Path("/{workspaceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWorkspace(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace workspace = store.getWorkspaces().get(workspaceId);
        if (workspace == null) {
            return notFound();
        }
        if (workspace.getModelIds() != null && !workspace.getModelIds().isEmpty()) {
            throw new WorkspaceNotEmptyException();
        }
        store.getWorkspaces().remove(workspaceId);
        return Response.noContent().build();
    }

    private Response notFound() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", "Workspace not found");
        return Response.status(Response.Status.NOT_FOUND).entity(body).build();
    }
}
