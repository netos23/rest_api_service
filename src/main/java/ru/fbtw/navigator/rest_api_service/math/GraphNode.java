package ru.fbtw.navigator.rest_api_service.math;

import ru.fbtw.navigator.rest_api_service.navigation.Level;
import ru.fbtw.navigator.rest_api_service.navigation.Node;

import java.util.ArrayList;
import java.util.Collections;

public class GraphNode {
	private Node baseNode;
	private ArrayList<Edge> connections;
	private double destination;
	private GraphNode prev;
	private boolean isFinal;
	@Deprecated
	private Level level;

	public GraphNode(Node baseNode) {
		this.baseNode = baseNode;
		connections = new ArrayList<>();
	}

	public void applyConnections(){
		Collections.sort(connections);
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean aFinal) {
		isFinal = aFinal;
	}

	@Deprecated
	public Level getLevel() {
		return level;
	}

	@Deprecated
	public void setLevel(Level level) {
		this.level = level;
	}

	public void setDestination(double destination) {
		this.destination = destination;
	}

	public Node getBaseNode() {
		return baseNode;
	}

	public ArrayList<Edge> getConnections() {
		return connections;
	}

	public double getDestination() {
		return destination;
	}

	public GraphNode getPrev() {
		return prev;
	}

	public void setPrev(GraphNode prev) {
		this.prev = prev;
	}


	public Edge getConnection(GraphNode other){
		for (Edge connection : connections) {
			if (connection.getOther(this).equals(other)){
				return connection;
			}
		}
		return null;
	}
}
