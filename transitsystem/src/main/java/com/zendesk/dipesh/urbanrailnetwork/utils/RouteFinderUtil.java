package com.zendesk.dipesh.urbanrailnetwork.utils;

import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.DATETIME_FORMAT;
import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.TIME_FORMAT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import com.zendesk.dipesh.urbanrailnetwork.entities.Node;
import com.zendesk.dipesh.urbanrailnetwork.entities.QueueNode;
import com.zendesk.dipesh.urbanrailnetwork.entities.StationMap;
import com.zendesk.dipesh.urbanrailnetwork.processors.GraphBuilder;

public class RouteFinderUtil {
	private static Map<String, StationMap> stationCodeToStationMapCache = GraphBuilder.getStationCodeToStationMap();

	/*
	 * Returns set of edges for a vertex in increasing order of arrival time per
	 * edge
	 */
	public static Set<QueueNode> getEdgesByWeight(Set<Node> adjStations, QueueNode currentNode, Set<String> visited,
			Map<String, Node> map) {
		Set<QueueNode> result = new TreeSet<>();
		Date currentDate = currentNode.getArrDateTime();
		String currentNodeCode = currentNode.getNode().getStationCode();
		String currentNodeCodeWithoutSeq = null;
		for (int i = 0; i < currentNodeCode.length();) {
			StringBuilder sb = new StringBuilder();
			while (i < currentNodeCode.length() && !Character.isDigit(currentNodeCode.charAt(i))) {
				sb.append(currentNodeCode.charAt(i++));
			}
			currentNodeCodeWithoutSeq = sb.toString();
			break;
		}
		for (Node node : adjStations) {
			if (!visited.contains(node.getStationCode()) && map.containsKey(node.getStationCode())
					&& isLineOpen(node.getStationCode(), currentDate)) {
				Date newNodeArrivalTime = getNextStationArrivalTime(node.getStationCode(), currentDate);
				if (!node.getStationCode().startsWith(currentNodeCodeWithoutSeq)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(newNodeArrivalTime);
					if (isPeakHour(currentDate)) {
						calendar.add(Calendar.MINUTE, 15);
					} else {
						calendar.add(Calendar.MINUTE, 10);
					}
					newNodeArrivalTime = calendar.getTime();
				}
				QueueNode newNode = new QueueNode(map.get(node.getStationCode()),
						currentNode.getPath() + ", " + node.getStationCode());
				newNode.setArrDateTime(newNodeArrivalTime);
				result.add(newNode);
			}
		}
		return result;
	}

	/*
	 * Returns true if line with particular code is functional at given input time.
	 * Takes station code and time as input
	 */
	public static boolean isLineOpen(String code, Date current) {
		if (stationCodeToStationMapCache.isEmpty())
			stationCodeToStationMapCache = GraphBuilder.getStationCodeToStationMap();
		if (stationCodeToStationMapCache.containsKey(code)) {
			try {
				Date stationStartDate = stationCodeToStationMapCache.get(code).getOpeningDate();
				SimpleDateFormat desiredDateFormat = new SimpleDateFormat(DATETIME_FORMAT);
				desiredDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				stationStartDate = desiredDateFormat.parse(desiredDateFormat.format(stationStartDate));
				stationStartDate = GraphBuilder.getDateAfterOffsetAdjust(stationStartDate);
				if (current.after(stationStartDate)) {
					if ((code.startsWith("DT") || code.startsWith("CG") || code.startsWith("CE"))
							&& isNightHour(current)) {
						return false;
					}
					return true;
				}
			} catch (ParseException e) {
				return false;
			}
		}
		return false;
	}

	/*
	 * Returns arrival time at next station after going through station having code
	 * as input code Takes station code and time as input
	 */
	public static Date getNextStationArrivalTime(String code, Date current) {
		int additionalMin = 0;
		if (isNightHour(current)) {
			if (code.startsWith("TE"))
				additionalMin += 8;
			else
				additionalMin += 10;
		} else if (isPeakHour(current)) {
			if (code.startsWith("NS") || code.startsWith("NE"))
				additionalMin += 12;
			else
				additionalMin += 10;
		} else {
			if (code.startsWith("DT") || code.startsWith("TE"))
				additionalMin += 8;
			else
				additionalMin += 10;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(current);
		calendar.add(Calendar.MINUTE, additionalMin);
		return calendar.getTime();
	}

	/*
	 * Returns true if given input time is peak hour as per problem definition Takes
	 * time as input
	 */
	protected static boolean isPeakHour(Date current) {
		boolean isBetween6AmAnd9Am = false;
		boolean isBetween6PmAnd9Pm = false;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(GraphBuilder.getDateAfterOffsetAdjust(current));
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				return false;

			SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(GraphBuilder.getDateAfterOffsetAdjust(timeFormat.parse("06:00:00")));

			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(GraphBuilder.getDateAfterOffsetAdjust(timeFormat.parse("09:00:00")));

			calendar.setTime(GraphBuilder.getDateAfterOffsetAdjust(GraphBuilder
					.getDateAfterOffsetAdjust(timeFormat.parse(new SimpleDateFormat(TIME_FORMAT).format(current)))));
			if ((calendar.getTime().after(calendar1.getTime()) || calendar.getTime().equals(calendar1.getTime()))
					&& (calendar.getTime().before(calendar2.getTime())
							|| calendar.getTime().equals(calendar2.getTime()))) {
				isBetween6AmAnd9Am = true;
			}

			calendar1.setTime(GraphBuilder.getDateAfterOffsetAdjust(timeFormat.parse("18:00:00")));
			calendar2.setTime(GraphBuilder.getDateAfterOffsetAdjust(timeFormat.parse("21:00:00")));
			if ((calendar.getTime().after(calendar1.getTime()) || calendar.getTime().equals(calendar1.getTime()))
					&& (calendar.getTime().before(calendar2.getTime())
							|| calendar.getTime().equals(calendar2.getTime()))) {
				isBetween6PmAnd9Pm = true;
			}
		} catch (ParseException e) {
			return false;
		}
		return isBetween6AmAnd9Am || isBetween6PmAnd9Pm;
	}

	/*
	 * Returns true if given input time is night hour as per problem definition
	 * Takes time as input
	 */
	protected static boolean isNightHour(Date current) {
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Calendar calendar1 = Calendar.getInstance();
			Date time1 = GraphBuilder.getDateAfterOffsetAdjust(timeFormat.parse("06:00:00"));
			calendar1.setTime(time1);
			calendar1.add(Calendar.DATE, 1);

			Calendar calendar2 = Calendar.getInstance();
			Date time2 = GraphBuilder.getDateAfterOffsetAdjust(timeFormat.parse("22:00:00"));
			calendar2.setTime(time2);

			Date currTime = timeFormat.parse(timeFormat.format(current));
			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTime(GraphBuilder.getDateAfterOffsetAdjust(currTime));

			if ((calendar3.getTime().before(calendar1.getTime()) || calendar3.getTime().equals(calendar1.getTime()))
					&& calendar3.getTime().after(calendar2.getTime())
					|| calendar3.getTime().equals(calendar2.getTime())) {
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}

	/*
	 * Extract the station code discarding sequence
	 */
	public static String getStationCodePrefix(String stationCode) {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		while (i < stationCode.length()) {
			if (!Character.isDigit(stationCode.charAt(i)))
				sb.append(stationCode.charAt(i++));
			else
				break;
		}
		return sb.toString();
	}
}
