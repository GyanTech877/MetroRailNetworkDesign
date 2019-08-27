package com.zendesk.dipesh.urbanrailnetwork.entities;

import java.util.HashSet;
import java.util.Set;

public class Node {
	private String stationCode;
	private String stationName;
	Set<Node> childrens;

	public Node(String stationCode, String stationName) {
		this.stationCode = stationCode;
		this.stationName = stationName;
		this.childrens = new HashSet<>();
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Set<Node> getChildrens() {
		return childrens;
	}

	public void setChildrens(Set<Node> childrens) {
		this.childrens = childrens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stationCode == null) ? 0 : stationCode.hashCode());
		result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (stationCode == null) {
			if (other.stationCode != null)
				return false;
		} else if (!stationCode.equals(other.stationCode))
			return false;
		if (stationName == null) {
			if (other.stationName != null)
				return false;
		} else if (!stationName.equals(other.stationName))
			return false;
		return true;
	}

}
