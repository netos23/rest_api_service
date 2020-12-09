package ru.fbtw.navigator.rest_api_service.navigation;

import java.util.HashMap;
import java.util.HashSet;

public class MultiLevelNode extends Node {

	private HashMap<Level, Node> joinedNodes;
	private boolean hasUpdate;

	public MultiLevelNode(Node parent) {
		super(
				parent.getX(),
				parent.getY(),
				parent.getDescription(),
				parent.getName(),
				parent.getParent(),
				parent.getType(),
				parent.isPrime()
		);

		joinedNodes = new HashMap<>();
		add(parent);
	}

	public HashMap<Level, Node> getJoinedNodes() {
		return joinedNodes;
	}

	public void add(Node other) {
		hasUpdate = true;
		joinedNodes.put(other.getParent(), other);

		// replace other Neighbour to primeNode
		for(Node otherNeighbour : other.getNeighbours()){
			otherNeighbour.getNeighbours().remove(other);
			otherNeighbour.getNeighbours().add(this);
		}
		// add other Neighbours to primeNode Neighbours
		addNeighbours(other.getNeighbours());

		// save current node to other`s parent level system
		other.getParent().getNodeSystem().remove(other);
		other.getParent().getNodeSystem().add(this);
	}

	@Override
	public HashSet<String> getPseudoNames() {

		if (hasUpdate) {
			joinedNodes.values()
					.stream()
					.filter(Node::isPrime)
					.map(Node::getName)
					.forEach(pseudoNames::add);
			hasUpdate = false;
		}

		return pseudoNames;
	}


	@Override
	public int getX(Level context) {
		return joinedNodes.get(context).getX();
	}

	@Override
	public int getY(Level context) {
		return joinedNodes.get(context).getY();
	}

	@Override
	public String getName(Level context) {
		return joinedNodes.get(context).getName();
	}

	@Override
	public Level getParent(Level context) {
		return joinedNodes.get(context).getParent();
	}

	@Override
	public NodeType getType(Level context) {
		return joinedNodes.get(context).getType();
	}

	@Override
	public boolean isPrime(Level context) {
		return joinedNodes.get(context).isPrime();
	}

	@Override
	public String getDescription(Level context) {
		return joinedNodes.get(context).getDescription();
	}

	/**
	 * @deprecated - Use {@link #getX(Level)} instead
	 * */
	@Deprecated
	@Override
	public int getX() {
		return super.getX();
	}

	/**
	 * @deprecated - Use {@link #getY(Level)} instead
	 * */
	@Deprecated
	@Override
	public int getY() {
		return super.getY();
	}

	/**
	 * @deprecated - Use {@link #getName(Level)}  instead
	 * */
	@Deprecated
	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * @deprecated - Use {@link #getParent(Level)}  instead
	 * */
	@Deprecated
	@Override
	public Level getParent() {
		return super.getParent();
	}

	/**
	 * @deprecated - Use {@link #getX(Level)} instead
	 * */
	@Deprecated
	@Override
	public NodeType getType() {
		return super.getType();
	}

	/**
	 * @deprecated - Use {@link #isPrime(Level)} instead
	 * */
	@Deprecated
	@Override
	public boolean isPrime() {
		return super.isPrime();
	}

	/**
	 * @deprecated - Use {@link #getDescription(Level)} instead
	 * */
	@Deprecated
	@Override
	public String getDescription() {
		return super.getDescription();
	}
}
