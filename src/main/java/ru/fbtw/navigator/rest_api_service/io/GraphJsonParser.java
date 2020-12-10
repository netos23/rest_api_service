package ru.fbtw.navigator.rest_api_service.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import ru.fbtw.navigator.rest_api_service.navigation.Level;
import ru.fbtw.navigator.rest_api_service.navigation.MultiLevelNode;
import ru.fbtw.navigator.rest_api_service.navigation.Node;
import ru.fbtw.navigator.rest_api_service.navigation.NodeType;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GraphJsonParser {
    private JsonObject root;


    private ArrayList<Level> levels;

    public GraphJsonParser(String json) {
        Reader reader = new StringReader(json);
        root = JsonParser.parseReader(reader).getAsJsonObject();
    }

    public GraphJsonParser(File json) throws FileNotFoundException {
        Reader reader = new FileReader(json);
        root = JsonParser.parseReader(reader).getAsJsonObject();

    }

    public HashMap<String, Node> parse(Set<Node> nodes) throws IOException {
        JsonArray levelList = root.getAsJsonArray("levels");

        levels = parseLevels(levelList);
        JsonArray levelConnections = root.getAsJsonArray("connections");

        buildGlobalNodeSystem(levelConnections);

        return exportNodes(nodes);
    }

    private HashMap<String, Node> exportNodes(Set<Node> nodes) {

        return (HashMap<String, Node>) levels.stream()
                .flatMap(level -> level.getNodeSystem().stream())
                .filter(node -> {
                    nodes.add(node);
                    return node.getType() != NodeType.TEMP;
                })
                .flatMap(node -> node.getPseudoNames()
                        .stream()
                        .collect(Collectors.toMap(s -> s, s -> node))
                        .entrySet()
                        .stream())
                .collect(Collectors
                        .toMap(key -> key.getKey().toLowerCase(Locale.ROOT),
                                Map.Entry::getValue, (existing, replacement) -> existing));
    }


    private void buildGlobalNodeSystem(JsonArray levelConnection) {
        for (JsonElement connection : levelConnection) {
            JsonObject connectionObject = connection.getAsJsonObject();
            String levelNameA = connectionObject.get("nodeA").getAsString();
            String socketNameA = connectionObject.get("socketA").getAsString();
            String levelNameB = connectionObject.get("nodeB").getAsString();
            String socketNameB = connectionObject.get("socketB").getAsString();
            try {
                Node socketA = getSocketByName(levelNameA, socketNameA);
                Node socketB = getSocketByName(levelNameB, socketNameB);

                MultiLevelNode node = new MultiLevelNode(socketA);
                node.add(socketB);
            } catch (NullPointerException ex) {
                //todo: лог об ощибке соединения нодов
            }
        }
    }

    private Node getSocketByName(String levelName, String socketName) {
        return Objects.requireNonNull(levels.stream()
                .filter(level -> level.getName().equals(levelName))
                .findFirst()
                .orElse(null))
                .getNodeSystem()
                .stream()
                .filter(socket -> socket.getName().equals(socketName))
                .findFirst()
                .orElse(null);
    }

    public ArrayList<Level> parseLevels(JsonArray levelList) throws IOException {
        ArrayList<Level> res = new ArrayList<>();

        for (int i = 0; i < levelList.size(); i++) {
            JsonObject levelJsonObj = levelList.get(i).getAsJsonObject();
            res.add(parseLevel(levelJsonObj));
        }

        return res;
    }


    public Level parseLevel(JsonObject levelJsonObj) throws IOException {
        String name = levelJsonObj.get("name").getAsString();

        String base64Image = levelJsonObj.get("image").getAsString();
        //BufferedImage image = ImageUtils.imageFromBase64(base64Image);

        Level level = new Level(name, base64Image);

        JsonObject nodeSystemJson = levelJsonObj.getAsJsonObject("node_system");
        ArrayList<Node> nodeSystem = parseNodeSystem(nodeSystemJson, level);
        level.setNodeSystem(nodeSystem);

        return level;
    }

    private ArrayList<Node> parseNodeSystem(JsonObject nodeSystemJson, Level level) {
        JsonArray nodesJson = nodeSystemJson.getAsJsonArray("nodes");
        HashMap<String, Node> nodesMap = getNodes(nodesJson, level);

        JsonArray connectionsJson = nodeSystemJson.getAsJsonArray("connections");
        connectNodes(nodesMap, connectionsJson);

        return new ArrayList<>(nodesMap.values());
    }

    private void connectNodes(HashMap<String, Node> nodesMap, JsonArray connectionsJson) {
        for (JsonElement element : connectionsJson) {
            String nameA = element.getAsJsonObject().get("a").getAsString();
            Node a = nodesMap.get(nameA);
            String nameB = element.getAsJsonObject().get("b").getAsString();
            Node b = nodesMap.get(nameB);

            Node.connect(a, b);
        }
    }

    private HashMap<String, Node> getNodes(JsonArray nodesJson, Level level) {
        HashMap<String, Node> result = new HashMap<>();

        for (JsonElement element : nodesJson) {
            Node node = parseNode(element.getAsJsonObject(), level);
            result.put(node.getName(), node);
        }

        return result;
    }

    private Node parseNode(JsonObject element, Level parent) {
        String name = element.get("name").getAsString();
        String description = element.get("description").getAsString();

        int x = element.get("x").getAsInt();
        int y = element.get("y").getAsInt();

        NodeType nodeType = NodeType.values()[element.get("type").getAsInt()];
        boolean isPrime = nodeType == NodeType.ZONE_CONNECTION && element.get("isPrime").getAsBoolean();

        return new Node(x, y, description, name, parent, nodeType, isPrime);
    }


}
