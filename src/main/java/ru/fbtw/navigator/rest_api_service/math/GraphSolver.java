package ru.fbtw.navigator.rest_api_service.math;

import lombok.extern.slf4j.Slf4j;
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
	private HashMap<String, Node> nodesStorage;

	/**
	 * List of {@link GraphNode} for searching in the graph of the nodes
	 */
	private ArrayList<GraphNode> graphNodes;

	/**
	 * Unique nodes, includes node of type{@code NodeType.TEMP}
	 */
	private HashSet<Node> uniqueNodes;

	/**
	 * Key-value storage of GraphNodes for fast searching by Node
	 */
	private HashMap<Node, GraphNode> graphNodeStorage;

	private GraphNodeComparator comparator;


	public GraphSolver(HashMap<String, Node> nodesStorage, HashSet<Node> privateNodes) {
		this.nodesStorage = nodesStorage;
		uniqueNodes = privateNodes;
		comparator = new GraphNodeComparator();
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
		graphNodeStorage = new HashMap<>();
		for (Node node : uniqueNodes) {
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

	public List<GraphNode> getPath(String targetName, String startName) throws Exception {
		Node target = nodesStorage.get(targetName);
		Node start = nodesStorage.get(startName);

		if (target != null && start != null) {
			return searchPath(target, start);
		} else {
			return null;
		}
	}

	private List<GraphNode> searchPath(Node target, Node origin) throws Exception{
		restoreNodes();

		GraphNode start = graphNodeStorage.get(origin);
		GraphNode finish = graphNodeStorage.get(target);

		start.setDestination(0);
		graphNodes.sort(comparator);

		while (!graphNodes.get(0).isFinal()) {
			GraphNode selected = graphNodes.get(0);
			for (Edge edge : selected.getConnections()) {
				if (!edge.isChecked()) {
					GraphNode other = edge.getOther(selected);
					edge.setChecked(true);
					if (other.getDestination() > selected.getDestination() + edge.getLength()) {
						other.setDestination(selected.getDestination() + edge.getLength());
						other.setPrev(selected);
					}
				}
			}
			selected.setFinal(true);

			graphNodes.sort(comparator);
		}

		LinkedList<GraphNode> path = new LinkedList<>();
		path.add(finish);
		GraphNode prev;

		do{
			prev = path.peekLast().getPrev();
			if(prev != null){
				path.add(prev);
			}else{
				if(!path.peekLast().equals(start)){
					throw new Exception("Wrong path");
				}
			}
		}while (prev != null);

		return path;
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
}
