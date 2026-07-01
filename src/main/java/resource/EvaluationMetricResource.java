package resource;

import exception.ModelDeprecatedException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.EvaluationMetric;
import model.MachineLearningModel;
import storage.DataStore;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Handles GET/POST for /api/v1/models/{modelId}/metrics
 * Instantiated by ModelResource's sub-resource locator with the
 * modelId already bound, so no further path injection is required.
 */
public class EvaluationMetricResource {

    private final String modelId;
    private final DataStore store = DataStore.getInstance();

    public EvaluationMetricResource(String modelId) {
        this.modelId = modelId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetrics() {
        MachineLearningModel model = store.getModels().get(modelId);
        if (model == null) {
            return notFound();
        }
        List<EvaluationMetric> metrics = store.getMetricsByModelId()
                .getOrDefault(modelId, new ArrayList<>());
        return Response.ok(metrics).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMetric(EvaluationMetric metric) {
        MachineLearningModel model = store.getModels().get(modelId);
        if (model == null) {
            return notFound();
        }

        if ("DEPRECATED".equalsIgnoreCase(model.getStatus())) {
            throw new ModelDeprecatedException();
        }

        String id = UUID.randomUUID().toString();
        metric.setId(id);
        if (metric.getTimestamp() == 0L) {
            metric.setTimestamp(System.currentTimeMillis());
        }

        store.getMetricsByModelId()
                .computeIfAbsent(modelId, k -> new ArrayList<>())
                .add(metric);

        model.setLatestAccuracy(metric.getAccuracyScore());

        return Response.status(Response.Status.CREATED).entity(metric).build();
    }

    private Response notFound() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", "Model not found");
        return Response.status(Response.Status.NOT_FOUND).entity(body).build();
    }
}
