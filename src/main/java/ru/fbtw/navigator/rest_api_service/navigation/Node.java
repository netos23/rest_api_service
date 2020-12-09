package ru.fbtw.navigator.rest_api_service.navigation;

import java.util.Collection;
import java.util.HashSet;

public class Node {
	protected HashSet<String> pseudoNames;
	private int x, y;
	private String name;
	private String description;
	private Level parent;
	private NodeType type;
	private boolean isPrime;
	private HashSet<Node> neighbours;

	public Node(
			int x, int y,
			String description,
			String name,
			Level parent,
			NodeType type,
			boolean isPrime
	) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.description = description;
		this.parent = parent;
		this.type = type;
		this.isPrime = isPrime;

		pseudoNames = new HashSet<>();
		pseudoNames.add(name);
		neighbours = new HashSet<>();
	}

	public static void connect(Node a, Node b) {
		a.neighbours.add(b);
		b.neighbours.add(a);
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	public Level getParent() {
		return parent;
	}

	public NodeType getType() {
		return type;
	}

	public boolean isPrime() {
		return isPrime;
	}

	public String getDescription() {
		return description;
	}

	public HashSet<String> getPseudoNames() {
		return pseudoNames;
	}

	public HashSet<Node> getNeighbours() {
		return neighbours;
	}

	public void addNeighbours(Collection<Node> nodes) {
		neighbours.addAll(nodes);
	}

	public int getX(Level context) {
		return getX();
	}

	public int getY(Level context) {
		return getY();
	}

	public String getName(Level context) {
		return getName();
	}

	public Level getParent(Level context) {
		return getParent();
	}

	public NodeType getType(Level context) {
		return getType();
	}

	public boolean isPrime(Level context) {
		return isPrime();
	}

	public String getDescription(Level context) {
		return getDescription();
	}
}
