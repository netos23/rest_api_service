package ru.fbtw.navigator.rest_api_service.math;

import java.util.Comparator;

public class GraphNodeComparator implements Comparator<GraphNode> {

	@Override
	public int compare(GraphNode o1, GraphNode o2) {
		if (o1.isFinal() && o2.isFinal()) {
			return 0;
		} else if (o1.isFinal()) {
			return 1;
		} else if (o2.isFinal()) {
			return -1;
		} else {
			return Double.compare(o1.getDestination(), o2.getDestination());
		}
	}
}
