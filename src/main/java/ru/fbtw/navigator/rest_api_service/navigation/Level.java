package ru.fbtw.navigator.rest_api_service.navigation;

import java.util.ArrayList;

public class Level {
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



}
