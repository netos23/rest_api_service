package ru.fbtw.navigator.rest_api_service.math;

import lombok.extern.slf4j.Slf4j;
import ru.fbtw.navigator.rest_api_service.navigation.Level;
import ru.fbtw.navigator.rest_api_service.navigation.Node;

import java.util.*;

@Slf4j
public class GraphSolver {

	/**
	 * Shows that every node can be reached from any node
	 */
	private boolean isSecure;

	/**
	 * Key-value storage of nodes for fast searching by name
	 */
	private Map<String, Node> nodesStorage;

	/**
	 * List of {@link GraphNode} for searching in the graph of the nodes
	 */
	private ArrayList<GraphNode> graphNodes;

	/**
	 * Unique nodes, includes node of type{@code NodeType.TEMP}
	 */
	private Set<Node> uniqueNodes;
	private Set<Level> levels;
	private Set<Edge> edges;


	public GraphSolver(Map<String, Node> nodesStorage, Set<Node> privateNodes) {
		this.nodesStorage = nodesStorage;
		uniqueNodes = privateNodes;

		levels = new HashSet<>();
		edges = new HashSet<>();

		//testSecurity();
		if(!isSecure){
			log.warn("The node system is not closed. This can lead to errors");
		}
		initGraph();
	}

	private static <T> T getFirstFormSet(Set<T> collection) {
		return collection.iterator().next();
	}

	public boolean testSecurity() {
		HashSet<Node> destinations = new HashSet<>(uniqueNodes);

		LinkedList<Node> queue = new LinkedList<>();
		queue.add(getFirstFormSet(destinations));

		while (!queue.isEmpty()) {
			Node el = queue.pollFirst();
			//uniqueNodes.add(el);
			if (el != null && destinations.remove(el)) {
				queue.addAll(el.getNeighbours());
			}
		}

		return (isSecure = destinations.isEmpty());
	}

	private void initGraph() {
		// setup nodes
		graphNodes = new ArrayList<>();
		/**
		 * Key-value storage of GraphNodes for fast searching by Node
		 */
		HashMap<Node, GraphNode> graphNodeStorage = new HashMap<>();
		for (Node node : uniqueNodes) {
			levels.add(node.getParent());
			GraphNode graphNode = new GraphNode(node);
			graphNodes.add(graphNode);
			graphNodeStorage.put(node, graphNode);
		}

		// setup connections
		for (Node node : uniqueNodes) {
			for (Node neighbour : node.getNeighbours()) {
				GraphNode nodeA = graphNodeStorage.get(node);
				GraphNode nodeB = graphNodeStorage.get(neighbour);

				Edge edge = new Edge(nodeA, nodeB);
				edges.add(edge);
				if(!nodeA.getConnections().contains(edge)) {
					nodeA.getConnections().add(edge);
					nodeB.getConnections().add(edge);
				}
			}
		}

		// sort connections by length
		for (GraphNode node : graphNodes) {
			node.applyConnections();
		}

		restoreNodes();
	}


	private void restoreNodes() {
		for (GraphNode node : graphNodes) {
			for (Edge edge : node.getConnections()) {
				edge.setChecked(false);
			}
			node.setFinal(false);
			node.setPrev(null);
			node.setDestination(Double.POSITIVE_INFINITY);
		}
	}

	public boolean isSecure() {
		return isSecure;
	}

	public Set<Level> getLevels() {
		return levels;
	}

	public Set<Edge> getEdges() {
		return edges;
	}
}
