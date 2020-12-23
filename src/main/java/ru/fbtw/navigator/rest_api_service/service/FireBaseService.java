package ru.fbtw.navigator.rest_api_service.service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class FireBaseService implements PlatformService{
	private final String LEVEL = "levels";
	private final String NODES = "nodes";
	private final String CONNECTIONS = "connections";

	private final Firestore db;

	@SneakyThrows
	public FireBaseService(@Value("${project.id}") String projectId) {
		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
				.setProjectId(projectId)
				.setCredentials(GoogleCredentials.getApplicationDefault())
				.build();

		db = firestoreOptions.getService();

	}

	private void setupCollections(
			String json,
			Set<Level> levels,
			Set<Node> nodes,
			Set<Edge> edges
	) {
		try {
			GraphJsonParser parser = new GraphJsonParser(json);
			Map<String, Node> nodesStorage = parser.parse(nodes);

			GraphSolver graph = new GraphSolver(nodesStorage, nodes);

			levels.addAll(graph.getLevels());
			edges.addAll(graph.getEdges());


		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error while paring json");
		}
	}

	@Override
	public void updateData(String projectId, String json, boolean isNew) {
		if (isNew) {
			createUser(projectId, json);
		} else {
			updateUser(projectId, json);
		}
	}

	public void createUser(String projectId, String json) {

		Set<Level> levels = new HashSet<>();
		Set<Node> nodes = new HashSet<>();
		Set<Edge> edges = new HashSet<>();
		setupCollections(json, levels, nodes, edges);

		createUser(projectId, levels, nodes, edges);
	}

	public void updateUser(String projectId, String json) {

		Set<Level> levels = new HashSet<>();
		Set<Node> nodes = new HashSet<>();
		Set<Edge> edges = new HashSet<>();
		setupCollections(json, levels, nodes, edges);

		updateUser(projectId, levels, nodes, edges);
	}

	void createUser(
			String projectId,
			Set<Level> levels,
			Set<Node> nodes,
			Set<Edge> edges
	) {
		uploadCollection(levels, LEVEL, projectId);
		uploadCollection(nodes, NODES, projectId);
		uploadCollection(edges, CONNECTIONS, projectId);
	}

	void remove(String projectId) {
		removeCollection(projectId, LEVEL);
		removeCollection(projectId, NODES);
		removeCollection(projectId, CONNECTIONS);
	}

	public void updateUser(
			String projectId,
			Set<Level> levels,
			Set<Node> nodes,
			Set<Edge> edges
	) {
		remove(projectId);
		createUser(projectId, levels, nodes, edges);
	}

	private void removeCollection(String projectId, String type) {
		CollectionReference collection = db.collection(projectId)
				.document(type)
				.collection(type);
		Iterable<DocumentReference> documentReferences = collection.listDocuments();

		log.info("Removing collection for {} type: {}", projectId, type);

		for (DocumentReference reference : documentReferences) {
			try {
				ApiFuture<WriteResult> delete = reference.delete();
				log.info("Remove time: {} for doc id: {} project's: {}", delete.get().getUpdateTime(),
						reference.getId(), projectId);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				log.error("Error while deleting");
			}

		}
	}


	private void uploadCollection(Set<? extends Mappable> set, String type, String projectId) {
		CollectionReference collectionReference = db.collection(projectId).document(type).collection(type);

		for (Mappable object : set) {
			try {

				Map<String, Object> map = object.toMap();

				ApiFuture<DocumentReference> result = collectionReference.add(map);

				DocumentReference reference = result.get();
				log.info("Upload doc id: {}  project's: {}", reference.getId(), projectId);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				log.error("Error while uploading");
			}

		}
	}

}
