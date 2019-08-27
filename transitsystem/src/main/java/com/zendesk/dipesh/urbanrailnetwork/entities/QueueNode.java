package com.zendesk.dipesh.urbanrailnetwork.entities;

import java.util.Date;

public class QueueNode implements Comparable<QueueNode> {
	private Node node;
	private String path;
	private Date arrDateTime;

	public QueueNode(Node node, String path) {
		super();
		this.node = node;
		this.path = path;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getArrDateTime() {
		return arrDateTime;
	}

	public void setArrDateTime(Date arrDateTime) {
		this.arrDateTime = arrDateTime;
	}

	@Override
	public int compareTo(QueueNode queueNode) {
		if (this.getArrDateTime().before(queueNode.getArrDateTime()))
			return -1;
		else
			return 1;
	}

}
