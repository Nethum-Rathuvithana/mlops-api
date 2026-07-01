package resource;

import exception.LinkedWorkspaceNotFoundException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.MLWorkspace;
import model.MachineLearningModel;
import storage.DataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/models")
public class ModelResource {

    private final DataStore store = DataStore.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createModel(MachineLearningModel model) {
        String workspaceId = model.getWorkspaceId();
        MLWorkspace workspace = store.getWorkspaces().get(workspaceId);
        if (workspace == null) {
            throw new LinkedWorkspaceNotFoundException();
        }

        String id = UUID.randomUUID().toString();
        model.setId(id);

        store.getModels().put(id, model);
        store.getMetricsByModelId().put(id, new ArrayList<>());

        if (workspace.getModelIds() == null) {
            workspace.setModelIds(new ArrayList<>());
        }
        workspace.getModelIds().add(id);

        return Response.status(Response.Status.CREATED).entity(model).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MachineLearningModel> getAllModels(@QueryParam("status") String status) {
        if (status != null && !status.isEmpty()) {
            return store.getModels().values().stream()
                    .filter(m -> status.equalsIgnoreCase(m.getStatus()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(store.getModels().values());
    }

    /**
     * Sub-resource locator: delegates handling of
     * /models/{modelId}/metrics/** to EvaluationMetricResource.
     */
    @Path("/{modelId}/metrics")
    public EvaluationMetricResource getMetricsResource(@PathParam("modelId") String modelId) {
        return new EvaluationMetricResource(modelId);
    }
}
