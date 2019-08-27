package com.zendesk.dipesh.urbanrailnetwork.utils;

import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.DATETIME_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RouteFinderUtilTest {
	SimpleDateFormat format;

	@BeforeEach
	void setup() {
		format = new SimpleDateFormat(DATETIME_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Test
	void testGetStationCodePrefixPositive() {
		assertTrue("NS".equals(RouteFinderUtil.getStationCodePrefix("NS10")));
	}

	@Test
	void testGetStationCodePrefixNegative() {
		assertFalse("PS".equals(RouteFinderUtil.getStationCodePrefix("NS10")));
	}

	@Test
	void testIsNightHourPositive() {
		Date date;
		try {
			date = format.parse("2019-02-31 22:00:01");
			assertTrue(RouteFinderUtil.isNightHour(date));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	void testIsNightHourNegative() {
		Date date;
		try {
			date = format.parse("2019-01-31 12:00:00");
			assertFalse(RouteFinderUtil.isNightHour(date));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	void testIsPeakHourPositive() {
		Date date;
		try {
			date = format.parse("2019-08-26 07:00:00");
			assertTrue(RouteFinderUtil.isPeakHour(date));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	void testIsPeakHourNegative() {
		Date date;
		try {
			date = format.parse("2019-01-30 11:00:00");
			assertFalse(RouteFinderUtil.isPeakHour(date));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	void testGetNextStationArrivalTimePositive() {
		try {
			assertEquals(format.parse("2019-08-26 07:10:00"),
					RouteFinderUtil.getNextStationArrivalTime("TE2", format.parse("2019-08-26 07:00:00")));
			assertEquals(format.parse("2019-08-26 07:12:00"),
					RouteFinderUtil.getNextStationArrivalTime("NE2", format.parse("2019-08-26 07:00:00")));
			assertEquals(format.parse("2019-02-31 22:08:01"),
					RouteFinderUtil.getNextStationArrivalTime("TE2", format.parse("2019-02-31 22:00:01")));
			assertEquals(format.parse("2019-02-31 22:10:01"),
					RouteFinderUtil.getNextStationArrivalTime("NE2", format.parse("2019-02-31 22:00:01")));
			assertEquals(format.parse("2019-08-26 12:08:00"),
					RouteFinderUtil.getNextStationArrivalTime("TE2", format.parse("2019-08-26 12:00:00")));
			assertEquals(format.parse("2019-08-26 07:10:00"),
					RouteFinderUtil.getNextStationArrivalTime("SE2", format.parse("2019-08-26 07:00:00")));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	void testGetNextStationArrivalTimeNegative() {
		try {
			assertNotEquals(format.parse("2019-08-26 07:12:00"),
					RouteFinderUtil.getNextStationArrivalTime("TE2", format.parse("2019-08-26 07:00:00")));
			assertNotEquals(format.parse("2019-08-26 07:10:00"),
					RouteFinderUtil.getNextStationArrivalTime("NE2", format.parse("2019-08-26 07:00:00")));
			assertNotEquals(format.parse("2019-02-31 22:10:01"),
					RouteFinderUtil.getNextStationArrivalTime("TE2", format.parse("2019-02-31 22:00:01")));
			assertNotEquals(format.parse("2019-02-31 22:08:01"),
					RouteFinderUtil.getNextStationArrivalTime("NE2", format.parse("2019-02-31 22:00:01")));
			assertNotEquals(format.parse("2019-08-26 12:10:00"),
					RouteFinderUtil.getNextStationArrivalTime("TE2", format.parse("2019-08-26 12:00:00")));
			assertNotEquals(format.parse("2019-08-26 07:08:00"),
					RouteFinderUtil.getNextStationArrivalTime("SE2", format.parse("2019-08-26 07:00:00")));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	void testIsLineOpenPositive() {
		Date date;
		try {
			date = format.parse("2019-02-31 22:00:01");
			assertTrue(RouteFinderUtil.isLineOpen("NS14", date));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	void testIsLineOpenNegative() {
		Date date;
		try {
			date = format.parse("2019-02-31 22:00:01");
			assertFalse(RouteFinderUtil.isLineOpen("DT1", date));
		} catch (ParseException e) {
			fail();
		}
	}

}
