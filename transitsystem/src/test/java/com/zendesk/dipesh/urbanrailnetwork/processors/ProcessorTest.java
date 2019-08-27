package com.zendesk.dipesh.urbanrailnetwork.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zendesk.dipesh.urbanrailnetwork.entities.Graph;

class ProcessorTest {
	private PrintStream originalSystemOut;
	private ByteArrayOutputStream systemOutContent;
	private Graph graph;

	@BeforeEach
	void redirectSystemOutStream() {
		graph = GraphBuilder.build();
		originalSystemOut = System.out;
		systemOutContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(systemOutContent));
	}

	@AfterEach
	void restoreSystemOutStream() {
		System.setOut(originalSystemOut);
	}

	@Test
	void testProcessPositive() {
		Processor.process(graph, "Travel from Holland Village to Bugis");
		assertEquals(systemOutContent.toString().trim(), "Stations travelled: 8\n"
				+ "Route: (CC21, CC20, CC19, DT9, DT10, DT11, DT12, DT13, DT14)\n" + "\n"
				+ "Take CC line from Holland Village to Farrer Road\n"
				+ "Take CC line from Farrer Road to Botanic Gardens\n" + "Change from CC line to DT line\n"
				+ "Take DT line from Botanic Gardens to Stevens\n" + "Take DT line from Stevens to Newton\n"
				+ "Take DT line from Newton to Little India\n" + "Take DT line from Little India to Rochor\n"
				+ "Take DT line from Rochor to Bugis\n" + "\n" + "Stations travelled: 11\n"
				+ "Route: (CC21, CC22, EW21, EW20, EW19, EW18, EW17, EW16, EW15, EW14, EW13, EW12)\n" + "\n"
				+ "Take CC line from Holland Village to Buona Vista\n" + "Change from CC line to EW line\n"
				+ "Take EW line from Buona Vista to Commonwealth\n" + "Take EW line from Commonwealth to Queenstown\n"
				+ "Take EW line from Queenstown to Redhill\n" + "Take EW line from Redhill to Tiong Bahru\n"
				+ "Take EW line from Tiong Bahru to Outram Park\n" + "Take EW line from Outram Park to Tanjong Pagar\n"
				+ "Take EW line from Tanjong Pagar to Raffles Place\n"
				+ "Take EW line from Raffles Place to City Hall\n" + "Take EW line from City Hall to Bugis\n".trim());
	}

	@Test
	void testProcessNegative() {
		Processor.process(graph, "Travel from Holland Village to Bugis");
		assertNotEquals(systemOutContent.toString().trim(),
				"Stations travelled: 8\n" + "Route: (CC21, CC20, CC19, DT9, DT10, DT11, DT12, DT13, DT14)\n" + "\n"
						+ "Take CC line from Holland Village to Farrer Road\n"
						+ "Take CC line from Farrer Road to Botanic Gardens\n" + "Change from CC line to DT line\n"
						+ "Take DT line from Botanic Gardens to Stevens\n" + "Take DT line from Stevens to Newton\n"
						+ "Take DT line from Newton to Little India\n" + "Take DT line from Little India to Rochor\n"
						+ "Take DT line from Rochor to Bugis\n" + "\n".trim());
	}

}
