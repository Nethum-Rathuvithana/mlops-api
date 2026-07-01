package storage;

import model.EvaluationMetric;
import model.MLWorkspace;
import model.MachineLearningModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory data store backed by Java collections only.
 * No database, JPA, or JDBC is used anywhere in this project.
 */
public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final Map<String, MLWorkspace> workspaces = new ConcurrentHashMap<>();
    private final Map<String, MachineLearningModel> models = new ConcurrentHashMap<>();
    private final Map<String, List<EvaluationMetric>> metricsByModelId = new ConcurrentHashMap<>();

    private DataStore() {
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public Map<String, MLWorkspace> getWorkspaces() {
        return workspaces;
    }

    public Map<String, MachineLearningModel> getModels() {
        return models;
    }

    public Map<String, List<EvaluationMetric>> getMetricsByModelId() {
        return metricsByModelId;
    }
}
