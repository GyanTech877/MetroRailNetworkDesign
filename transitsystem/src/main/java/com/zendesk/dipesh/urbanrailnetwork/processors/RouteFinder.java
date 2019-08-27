package com.zendesk.dipesh.urbanrailnetwork.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.zendesk.dipesh.urbanrailnetwork.entities.Graph;
import com.zendesk.dipesh.urbanrailnetwork.entities.Node;
import com.zendesk.dipesh.urbanrailnetwork.entities.QueueNode;
import com.zendesk.dipesh.urbanrailnetwork.utils.RouteFinderUtil;

public class RouteFinder {

	List<String> paths;
	Set<String> visited;
	Map<Long, List<String>> pathsMapWithTime;
	private Map<String, List<String>> stationNameToCodeMapCache = GraphBuilder.getStationNameToCodeMap();

	public RouteFinder() {
		paths = new ArrayList<>();
		visited = new HashSet<>();
		pathsMapWithTime = new HashMap<>();
	}

	/*
	 * Returns a List<String> having all possible route combinations.
	 * 
	 * Takes source,destination and graph as input. Graph is building block of the network where we search for path from source to destination.
	 */
	
	public List<String> find(Graph graph, String source, String destination) {
		if (stationNameToCodeMapCache.containsKey(source)) {
			List<String> stationCodes = stationNameToCodeMapCache.get(source);
			for (String code : stationCodes) {
				visited.add(code);
				Queue<QueueNode> queue = new LinkedList<>();
				queue.add(new QueueNode(graph.getMap().get(code), code));
				while (!queue.isEmpty()) {
					QueueNode curr = queue.poll();
					String currPath = curr.getPath();
					if (curr.getNode().getStationName().equalsIgnoreCase(destination)) {
						paths.add(curr.getPath().toString());
					} else {
						Set<Node> adjStations = curr.getNode().getChildrens();
						for (Node node : adjStations) {
							if (!visited.contains(node.getStationCode())
									&& graph.getMap().containsKey(node.getStationCode())) {
								visited.add(node.getStationCode());
								queue.add(new QueueNode(graph.getMap().get(node.getStationCode()),
										currPath + ", " + node.getStationCode()));
							}
						}
					}
				}
			}
		}
		return paths;
	}

	/*
	 * Returns a map with key as time in millisecond (which signifies how much time it takes to reach destination from source) and value as List<String>
	 * which holds all possible route combinations.
	 * 
	 * Takes source,destination,start time and graph as input. Graph is building block of the network where we search for path from source to destination
	 * if we start at particular start time.
	 */
	public Map<Long, List<String>> findWithTrafficData(Graph graph, String source, String destination, Date startTime) {
		if (stationNameToCodeMapCache.containsKey(source)) {
			List<String> stationCodes = stationNameToCodeMapCache.get(source);
			for (String code : stationCodes) {
				if (RouteFinderUtil.isLineOpen(code, startTime)) {
					Date nextStationArrivalTime = RouteFinderUtil.getNextStationArrivalTime(code, startTime);
					visited.add(code);
					Queue<QueueNode> queue = new LinkedList<>();
					QueueNode queueNode = new QueueNode(graph.getMap().get(code), code);
					queueNode.setArrDateTime(nextStationArrivalTime);
					queue.add(queueNode);
					while (!queue.isEmpty()) {
						QueueNode curr = queue.poll();
						if (curr.getNode().getStationName().equalsIgnoreCase(destination)) {
							long totalTimeinMs = curr.getArrDateTime().getTime() - startTime.getTime();
							if (!pathsMapWithTime.containsKey(totalTimeinMs)) {
								pathsMapWithTime.put(totalTimeinMs,
										new ArrayList<>(Arrays.asList(curr.getPath().toString())));
							} else {
								pathsMapWithTime.get(totalTimeinMs).add(curr.getPath().toString());
							}
						} else {
							Set<Node> adjStations = curr.getNode().getChildrens();
							Set<QueueNode> edgesByWeight = RouteFinderUtil.getEdgesByWeight(adjStations, curr, visited,
									graph.getMap());
							for (QueueNode node : edgesByWeight) {
								visited.add(node.getNode().getStationCode());
								queue.add(node);
							}
						}
					}
				}
			}
		}
		return pathsMapWithTime;
	}
}
