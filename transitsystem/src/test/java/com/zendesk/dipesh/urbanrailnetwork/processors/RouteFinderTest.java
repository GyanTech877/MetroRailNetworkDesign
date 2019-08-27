package com.zendesk.dipesh.urbanrailnetwork.processors;

import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.DATETIME_FORMAT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Maps;
import com.zendesk.dipesh.urbanrailnetwork.entities.Graph;

class RouteFinderTest {

	Graph graph;
	RouteFinder routeFinder;
	SimpleDateFormat format;

	@BeforeEach
	void setup() {
		graph = GraphBuilder.build();
		routeFinder = new RouteFinder();
		format = new SimpleDateFormat(DATETIME_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Test
	void testFindPositive() {
		List<String> expected = new ArrayList<>();
		expected.add("CC21, CC20, CC19, DT9, DT10, DT11, DT12, DT13, DT14");
		expected.add("CC21, CC22, EW21, EW20, EW19, EW18, EW17, EW16, EW15, EW14, EW13, EW12");
		assertArrayEquals(expected.toArray(), routeFinder
				.find(graph, "Holland Village".toLowerCase().trim(), "Bugis".toLowerCase().trim()).toArray());
	}

	@Test
	void testFindNegative() {
		List<String> expected = new ArrayList<>();
		expected.add("CC21, CC20, CC19, DT9, DT10, DT11, DT12, DT13, DT14");
		assertThat(expected.toArray(), IsNot.not(IsEqual.equalTo(routeFinder
				.find(graph, "Holland Village".toLowerCase().trim(), "Bugis".toLowerCase().trim()).toArray())));
	}

	@Test
	void testFindWithTrafficDataPositive() {
		Date date;
		try {
			date = GraphBuilder.getDateAfterOffsetAdjust(format.parse("2019-01-31 16:00:00"));
			Map<Long, List<String>> expected = new HashMap<>();
			expected.put((long) 5280000, Arrays.asList("CC21, CC20, CC19, DT9, DT10, DT11, DT12, DT13, DT14"));
			expected.put((long) 7800000,
					Arrays.asList("CC21, CC22, EW21, EW20, EW19, EW18, EW17, EW16, EW15, EW14, EW13, EW12"));
			assertThat(expected, is(routeFinder.findWithTrafficData(graph, "Holland Village".toLowerCase().trim(),
					"Bugis".toLowerCase().trim(), date)));

		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	void testFindWithTrafficDataNegative() {
		Date date;
		try {
			date = GraphBuilder.getDateAfterOffsetAdjust(format.parse("2019-01-31 16:00:00"));
			Map<Integer, List<String>> expected = new HashMap<>();
			expected.put(5280000, Arrays.asList("CC21, CC20, CC19, DT9, DT10, DT11, DT12, DT13, DT14"));
			assertFalse(Maps.difference(expected, routeFinder.findWithTrafficData(graph,
					"Holland Village".toLowerCase().trim(), "Bugis".toLowerCase().trim(), date)).areEqual());

		} catch (ParseException e) {
			fail();
		}
	}

}
