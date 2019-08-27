package com.zendesk.dipesh.urbanrailnetwork.processors;

import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.DATETIME_FORMAT;
import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.DATETIME_IDENTIFIER;
import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.DESTINATION_IDENTIFIER;
import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.INVALID_INPUTFORMAT_MESSAGE;
import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.SOURCE_IDENTIFIER;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.zendesk.dipesh.urbanrailnetwork.entities.Graph;
import com.zendesk.dipesh.urbanrailnetwork.utils.RoutePrinter;

public class Processor {

	/*
	 * Holds main logic for processing and printing routes for a given user input
	 */
	public static void process(Graph graph,String input){

		final Map<String,List<String>> stationNameToCodeMap=GraphBuilder.getStationNameToCodeMap();

		if(input.contains(SOURCE_IDENTIFIER) && input.contains(DESTINATION_IDENTIFIER)) {
			String source=input.substring(input.indexOf(SOURCE_IDENTIFIER)+5, input.indexOf(DESTINATION_IDENTIFIER)).toLowerCase().trim();
			String destination=input.substring(input.indexOf(DESTINATION_IDENTIFIER)+3).toLowerCase().trim();
			
			//This case is for finding paths including travel time between two stations depending on the starting time  
			if(input.contains(DATETIME_IDENTIFIER)) {
				String inputDateTime=input.substring(input.indexOf(DATETIME_IDENTIFIER)+9).toLowerCase().trim();
				destination=destination.substring(0,destination.indexOf(DATETIME_IDENTIFIER)-1).toLowerCase().trim();
				if(stationNameToCodeMap.containsKey(source) && stationNameToCodeMap.containsKey(destination)) {
					SimpleDateFormat format=new SimpleDateFormat(DATETIME_FORMAT);
					format.setTimeZone(TimeZone.getTimeZone("UTC"));
					Date startTime;
					try {
						startTime = format.parse(inputDateTime);
						startTime=GraphBuilder.getDateAfterOffsetAdjust(startTime);
						Map<Long, List<String>> resultWithTime=new RouteFinder().findWithTrafficData(graph,source,destination,startTime);
						RoutePrinter.printwithTime(resultWithTime);
					} catch (ParseException e) {
						System.out.println(INVALID_INPUTFORMAT_MESSAGE);
					}
				}
				else {
					System.out.println("No path exists from "+source+" to "+destination+" if you start at "+inputDateTime);
				}
			}
			
			//This case is for finding path between two stations without traffic being considered
			else {
				if(stationNameToCodeMap.containsKey(source) && stationNameToCodeMap.containsKey(destination)) {
					List<String> result=new RouteFinder().find(graph,source,destination);
					RoutePrinter.print(result,new StringBuilder());
				}
				else {
					System.out.println("No route is available between "+source+" and "+destination);
				}
			}

		}
		else {
			System.out.println(INVALID_INPUTFORMAT_MESSAGE);
		}

	}
}
