package com.zendesk.dipesh.urbanrailnetwork.processors;

import static com.zendesk.dipesh.urbanrailnetwork.constants.INetworkConstants.DATETIME_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphBuilderTest {
	SimpleDateFormat format;

	@BeforeEach
	void setup() {
		format = new SimpleDateFormat(DATETIME_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Test
	void testGetDateAfterOffsetAdjustPositive() {
		Date date;
		try {
			date = format.parse("2019-02-27 22:00:00");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR, -5);
			cal.add(Calendar.MINUTE, -30);
			assertEquals(cal.getTime(), GraphBuilder.getDateAfterOffsetAdjust(date));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void testGetDateAfterOffsetAdjustNegative() {
		Date date;
		try {
			date = format.parse("2019-02-27 22:00:00");
			assertNotEquals(date, GraphBuilder.getDateAfterOffsetAdjust(date));
		} catch (Exception e) {
			fail();
		}
	}

}
