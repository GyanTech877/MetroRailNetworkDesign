package com.zendesk.dipesh.urbanrailnetwork.utils;

import java.util.List;
import java.util.Map;

import com.zendesk.dipesh.urbanrailnetwork.entities.StationMap;
import com.zendesk.dipesh.urbanrailnetwork.processors.GraphBuilder;

public class RoutePrinter {

	static Map<String, StationMap> stationCodeToStationMap = GraphBuilder.getStationCodeToStationMap();

	/*
	 * Prints all paths as per format specification
	 */
	public static String print(List<String> result, StringBuilder output) {
		for (String path : result) {
			String[] stations = path.split(",");
			output.append("Stations travelled: " + (stations.length - 1) + "\n");
			output.append("Route: (" + path + ")\n\n");
			for (int i = 0; i < stations.length - 1; i++) {
				stations[i] = stations[i].trim();
				stations[i + 1] = stations[i + 1].trim();
				String first = RouteFinderUtil.getStationCodePrefix(stations[i]);
				String second = RouteFinderUtil.getStationCodePrefix(stations[i + 1]);
				if (first.equalsIgnoreCase(second)) {
					output.append(
							"Take " + first + " line from " + stationCodeToStationMap.get(stations[i]).getStationName()
							+ " to " + stationCodeToStationMap.get(stations[i + 1]).getStationName() + "\n");
				} else {
					output.append("Change from " + first + " line to " + second + " line\n");
				}
			}
			output.append("\n");
		}

		System.out.println(output.toString());
		return output.toString();
	}

	/*
	 * Prints all paths along with time taken as per format specification
	 */
	public static void printwithTime(Map<Long, List<String>> result) {
		for (Map.Entry<Long, List<String>> entry : result.entrySet()) {
			StringBuilder output = new StringBuilder();
			output.append("Time: " + entry.getKey() / 1000 + " seconds\n");
			print(entry.getValue(), output);
		}
	}
}
