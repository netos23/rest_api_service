package ru.fbtw.navigator.rest_api_service.service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.fbtw.navigator.rest_api_service.math.Edge;
import ru.fbtw.navigator.rest_api_service.navigation.Level;
import ru.fbtw.navigator.rest_api_service.navigation.Node;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class FireBaseService {
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

    public void createUser(
            String userId,
            Set<Level> levels,
            Set<Node> nodes,
            Set<Edge> edges
    ) {
        uploadCollection(levels, LEVEL, userId);
        uploadCollection(nodes, NODES, userId);
        uploadCollection(edges, CONNECTIONS, userId);
    }

    public void remove(String userId) {
        removeCollection(userId,LEVEL);
        removeCollection(userId,NODES);
        removeCollection(userId,CONNECTIONS);
    }

    public void updateUser(
            String userId,
            Set<Level> levels,
            Set<Node> nodes,
            Set<Edge> edges
    ) {
        remove(userId);
        createUser(userId, levels, nodes, edges);
    }

    private void removeCollection(String userId,String type) {
        CollectionReference collection = db.collection(userId)
                .document(type)
                .collection(type);
        Iterable<DocumentReference> documentReferences = collection.listDocuments();

        log.info("Removing collection for {} type: {}", userId,type);

        for (DocumentReference reference : documentReferences) {
            try {
                ApiFuture<WriteResult> delete = reference.delete();
                log.info("Remove time: {} for doc id: {} user's: {}", delete.get().getUpdateTime(),
                        reference.getId(), userId);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                log.error("Error while deleting");
            }

        }
    }


    private void uploadCollection(Set<? extends Mappable> set, String type, String userId) {
        CollectionReference collectionReference = db.collection(userId).document(type).collection(type);

        for (Mappable object : set) {
            try {

                Map<String, Object> map = object.toMap();

                ApiFuture<DocumentReference> result = collectionReference.add(map);

                DocumentReference reference = result.get();
                log.info("Upload doc id: {}  user's: {}", reference.getId(), userId);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                log.error("Error while uploading");
            }

        }
    }

}
