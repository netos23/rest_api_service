package ru.fbtw.navigator.rest_api_service.math;


import ru.fbtw.navigator.rest_api_service.navigation.Level;
import ru.fbtw.navigator.rest_api_service.navigation.MultiLevelNode;
import ru.fbtw.navigator.rest_api_service.navigation.Node;

public class Edge implements Comparable<Edge>{
	private GraphNode a;
	private GraphNode b;
	private Level level;
	private double length;
	private boolean isChecked;

	public Edge(GraphNode a, GraphNode b) {
		this.a = a;
		this.b = b;

		Node baseNodeA = a.getBaseNode();
		Node baseNodeB = b.getBaseNode();
		length = Double.MAX_VALUE;
		if (baseNodeA instanceof MultiLevelNode
				&& baseNodeB instanceof MultiLevelNode) {
			MultiLevelNode baseMultiNodeA = ((MultiLevelNode) baseNodeA);
			MultiLevelNode baseMultiNodeB = ((MultiLevelNode) baseNodeB);

			for(Level levelA : baseMultiNodeA.getJoinedNodes().keySet()){
				for(Level levelB : baseMultiNodeB.getJoinedNodes().keySet()){
					if(levelA.equals(levelB)) {
						double aX = baseNodeA.getX(levelA);
						double aY = baseNodeA.getY(levelA);
						Vector2 aPos = new Vector2(aX, aY);

						double bX = baseNodeB.getX(levelA);
						double bY = baseNodeB.getY(levelA);
						Vector2 bPos = new Vector2(bX, bY);

						double newLength = aPos.subtract(bPos).sqrMaginitude();
						if(length > newLength){
							length = newLength;
							level = levelA;
						}
					}
				}
			}
		} else {
			double aX = baseNodeA.getX(baseNodeB.getParent());
			double aY = baseNodeA.getY(baseNodeB.getParent());
			Vector2 aPos = new Vector2(aX, aY);

			double bX = baseNodeB.getX(baseNodeA.getParent());
			double bY = baseNodeB.getY(baseNodeA.getParent());
			Vector2 bPos = new Vector2(bX, bY);

			length = aPos.subtract(bPos).sqrMaginitude();

			if(baseNodeA instanceof MultiLevelNode){
				level = baseNodeB.getParent();
			}else {
				level = baseNodeA.getParent();
			}
		}
	}

	public GraphNode getA() {
		return a;
	}

	public GraphNode getB() {
		return b;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public Level getLevel() {
		return level;
	}

	@Override
	public int compareTo(Edge o) {
		return Double.compare(this.length,o.length);
	}

	public GraphNode getOther(GraphNode selected) {
		return a.equals(selected) ? b : a;
	}

	public double getLength() {
		return length;
	}

	@Override
	public boolean equals(Object obj) {
		if( obj instanceof Edge){
			Edge other = (Edge) obj;

			return other.a.equals(a) && other.b.equals(b)
					|| other.b.equals(a) && other.a.equals(b);
		}else{
			return false;
		}

	}
}
