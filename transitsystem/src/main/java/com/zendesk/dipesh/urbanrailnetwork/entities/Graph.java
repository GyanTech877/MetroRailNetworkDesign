package com.zendesk.dipesh.urbanrailnetwork.entities;

import java.util.HashMap;
import java.util.Map;

public class Graph {
	Map<String, Node> map;

	public Map<String, Node> getMap() {
		return map;
	}

	public Graph() {
		map = new HashMap<>();
	}

	public void addEdge(StationMap source, StationMap destination) {
		if (!source.getStationCode().equalsIgnoreCase(destination.getStationCode())) {
			Node currSourceGraphNode;
			if (map.containsKey(source.getStationCode())) {
				currSourceGraphNode = map.get(source.getStationCode());
			} else {
				currSourceGraphNode = new Node(source.getStationCode(), source.getStationName());
				map.put(source.getStationCode(), currSourceGraphNode);
			}
			currSourceGraphNode.childrens.add(new Node(destination.getStationCode(), destination.getStationName()));
			Node currDestinationGraphNode;
			if (map.containsKey(destination.getStationCode())) {
				currDestinationGraphNode = map.get(destination.getStationCode());
			} else {
				currDestinationGraphNode = new Node(destination.getStationCode(), destination.getStationName());
				map.put(destination.getStationCode(), currDestinationGraphNode);
			}
			currDestinationGraphNode.childrens.add(new Node(source.getStationCode(), source.getStationName()));
		}
	}
}
