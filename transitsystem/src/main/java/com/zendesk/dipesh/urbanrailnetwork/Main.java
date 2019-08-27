package com.zendesk.dipesh.urbanrailnetwork;

import java.util.Scanner;

import com.zendesk.dipesh.urbanrailnetwork.entities.Graph;
import com.zendesk.dipesh.urbanrailnetwork.processors.GraphBuilder;
import com.zendesk.dipesh.urbanrailnetwork.processors.Processor;

public class Main {
	public static void main(String[] args) {
		// Build network for first time while starting the application
		Graph graph = GraphBuilder.build();

		// Take input from users and keep on processing the input
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			Processor.process(graph, sc.nextLine());
		}
		sc.close();
	}

}
