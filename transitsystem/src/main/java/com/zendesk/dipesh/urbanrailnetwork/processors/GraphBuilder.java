package com.zendesk.dipesh.urbanrailnetwork.processors;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.opencsv.CSVReader;
import com.zendesk.dipesh.urbanrailnetwork.entities.Graph;
import com.zendesk.dipesh.urbanrailnetwork.entities.StationMap;
import com.zendesk.dipesh.urbanrailnetwork.exception.UrbanRailNetworkException;
import com.zendesk.dipesh.urbanrailnetwork.utils.RouteFinderUtil;

public class GraphBuilder {
	private static final String SAMPLE_CSV_FILE_PATH = "../StationMap.csv";

	private static List<StationMap> stationMapCache = new ArrayList<>();
	private static Map<String, List<StationMap>> connectionsCache = new HashMap<>();
	private static Map<String, StationMap> stationCodeToStationMapCache = new HashMap<>();;
	private static Map<String, List<String>> stationNameToCodeMapCache = new HashMap<>();

	public static Graph build() {
		Graph graph = new Graph();
		buildConnections();
		int curr = 0;
		while (curr < stationMapCache.size()) {
			String stationCode = stationMapCache.get(curr).getStationCode();
			String stationCodeWithoutSeq =RouteFinderUtil.getStationCodePrefix(stationCode);
			int next = curr + 1;
			while (next < stationMapCache.size() && stationCodeWithoutSeq != null
					&& stationMapCache.get(next).getStationCode().startsWith(stationCodeWithoutSeq)) {
				graph.addEdge(stationMapCache.get(curr), stationMapCache.get(next));
				curr++;
				next++;
			}
			curr = next;
		}
		for (List<StationMap> list : connectionsCache.values()) {
			if (list != null && !list.isEmpty()) {
				curr = 0;
				while (curr < list.size() - 1) {
					graph.addEdge(list.get(curr), list.get(curr + 1));
					curr++;
				}
			}
		}
		return graph;
	}

	/*
	 * Reads from sample CSV file and converts those data into List of StationMap objects 
	 */
	public static List<StationMap> buildStationMap() throws UrbanRailNetworkException {
		// Mapping file is already there in cache
		if (stationMapCache.size() > 0)
			return stationMapCache;
		try (Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
				CSVReader csvReader = new CSVReader(reader);) {
			String[] nextRecord;
			csvReader.readNext();
			SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
			while ((nextRecord = csvReader.readNext()) != null) {
				StationMap stationMap = new StationMap(nextRecord[0], nextRecord[1], format.parse(nextRecord[2]));
				stationMapCache.add(stationMap);
			}
			reader.close();
			csvReader.close();
		} catch (IOException | ParseException e) {
			throw new UrbanRailNetworkException(
					"Got error while parsing StationMap, please enter a valid csv file having format Station Code,Station Name,Opening Date");
		}
		Collections.sort(stationMapCache);
		return stationMapCache;
	}

	/*
	 * Reads from StationMap object and build a map where connections are stored 
	 */
	public static Map<String, List<StationMap>> buildConnections() {
		if (connectionsCache.size() > 0)
			return connectionsCache;
		try {
			List<StationMap> stationMaps = buildStationMap();
			Map<String, List<StationMap>> connections = new HashMap<>();
			for (int i = 0; i < stationMaps.size(); i++) {
				for (int j = 0; j < stationMaps.size(); j++) {
					if (i != j && stationMaps.get(i).getStationName()
							.equalsIgnoreCase(stationMaps.get(j).getStationName())) {
						if (connections.containsKey(stationMaps.get(i).getStationName())) {
							List<StationMap> list = connections.get(stationMaps.get(i).getStationName());
							list.add(stationMaps.get(j));
						} else {
							List<StationMap> list = new ArrayList<>();
							list.add(stationMaps.get(i));
							list.add(stationMaps.get(j));
							connections.put(stationMaps.get(i).getStationName(), list);
						}
					}
				}
			}
			connectionsCache = connections;
		} catch (UrbanRailNetworkException e) {
		}
		return connectionsCache;
	}

	/*
	 * Reads from StationMap object and constructs a map having key as StationCode and value ad StationMap object
	 */
	public static Map<String, StationMap> getStationCodeToStationMap() {
		if (stationCodeToStationMapCache.size() > 0)
			return stationCodeToStationMapCache;
		try {
			List<StationMap> stationMaps = buildStationMap();
			for (StationMap stationMap : stationMaps) {
				stationCodeToStationMapCache.put(stationMap.getStationCode(), stationMap);
			}
		} catch (UrbanRailNetworkException e) {
		}
		return stationCodeToStationMapCache;

	}

	/*
	 * Reads from StationMap object and constructs a map having key as StationName and value ad StationMap object
	 */
	public static Map<String, List<String>> getStationNameToCodeMap() {

		if (stationNameToCodeMapCache.size() > 0)
			return stationNameToCodeMapCache;
		try {
			List<StationMap> stationMaps = buildStationMap();
			Map<String, List<String>> stationNameToCode = new HashMap<>();
			for (int i = 0; i < stationMaps.size(); i++) {
				if (stationNameToCode.containsKey(stationMaps.get(i).getStationName().toLowerCase().trim())) {
					List<String> list = stationNameToCode.get(stationMaps.get(i).getStationName().toLowerCase().trim());
					list.add(stationMaps.get(i).getStationCode());
				} else {
					List<String> list = new ArrayList<>();
					list.add(stationMaps.get(i).getStationCode());
					stationNameToCode.put(stationMaps.get(i).getStationName().toLowerCase().trim(), list);
				}
			}
			stationNameToCodeMapCache = stationNameToCode;
		} catch (UrbanRailNetworkException e) {
		}
		return stationNameToCodeMapCache;
	}

	/*
	 * Returns a new date after adjusting offset value for given input date
	 */
	public static Date getDateAfterOffsetAdjust(Date currDate) {
		TimeZone tz = TimeZone.getDefault();
		Calendar cal = GregorianCalendar.getInstance(tz);
		int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
		Calendar calender = Calendar.getInstance();
		calender.setTime(currDate);
		calender.add(Calendar.MILLISECOND, -offsetInMillis);
		return calender.getTime();
	}
}
