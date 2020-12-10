package ru.fbtw.navigator.rest_api_service.navigation;

import ru.fbtw.navigator.rest_api_service.service.Mappable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Level implements Mappable {
	private String image;
	private String name;

	private ArrayList<Node> nodeSystem;

	public Level(String name, String image, ArrayList<Node> nodeSystem) {
		this.name = name;
		this.image = image;
		this.nodeSystem = nodeSystem;
	}

	public Level(String name, String image) {
		this.name = name;
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public String getName() {
		return name;
	}


	public void setNodeSystem(ArrayList<Node> nodeSystem) {
		this.nodeSystem = nodeSystem;
	}

	public ArrayList<Node> getNodeSystem() {
		return nodeSystem;
	}


	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();

		map.put("name", name);
		map.put("image",image);

		return map;
	}
}
