package ru.fbtw.navigator.rest_api_service.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fbtw.navigator.rest_api_service.io.GraphJsonParser;
import ru.fbtw.navigator.rest_api_service.math.Edge;
import ru.fbtw.navigator.rest_api_service.math.GraphSolver;
import ru.fbtw.navigator.rest_api_service.navigation.Level;
import ru.fbtw.navigator.rest_api_service.navigation.Node;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class UpdateService {

    private Set<Node> nodes;
    private Set<Edge> edges;
    private Set<Level> levels;
    private FireBaseService fireBaseService;

    public UpdateService(FireBaseService fireBaseService) {
        this.fireBaseService = fireBaseService;
        nodes = new HashSet<>();
        edges = new HashSet<>();
        levels = new HashSet<>();
    }

    private void clear() {
        nodes.clear();
        edges.clear();
        levels.clear();
    }

    public boolean updateMap(String userId, String jsonBody, boolean isNew) {
        setupCollections(jsonBody);

        if (isNew) {
            fireBaseService.createUser(userId, levels, nodes, edges);
        } else {
            fireBaseService.updateUser(userId, levels, nodes, edges);
        }

        // todo: пробросить исключения
        return true;
    }

    private void setupCollections(String json) {
        try {
            clear();

            GraphJsonParser parser = new GraphJsonParser(json);
            Map<String, Node> nodesStorage = parser.parse(nodes);

            GraphSolver graph = new GraphSolver(nodesStorage, nodes);

            levels = graph.getLevels();
            edges = graph.getEdges();


        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error while paring json");
        }
    }
}
