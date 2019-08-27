package com.zendesk.dipesh.urbanrailnetwork.entities;

import java.util.Date;

public class StationMap implements Comparable<StationMap> {
	private String stationCode;
	private String stationName;
	private Date openingDate;

	public StationMap(String stationCode, String stationName, Date openingDate) {
		this.stationCode = stationCode;
		this.stationName = stationName;
		this.openingDate = openingDate;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
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

	@Override
	public int compareTo(StationMap stationMap) {
		String currCodeWithoutSeq = null;
		int i = 0;
		while (i < this.getStationCode().length()) {
			StringBuilder sb = new StringBuilder();
			while (i < this.getStationCode().length() && !Character.isDigit(this.getStationCode().charAt(i))) {
				sb.append(this.getStationCode().charAt(i++));
			}
			currCodeWithoutSeq = sb.toString();
			break;
		}
		int currCodeSeq = Integer.valueOf(this.getStationCode().substring(i));

		String newCodeWithoutSeq = null;
		int j = 0;
		while (j < stationMap.getStationCode().length()) {
			StringBuilder sb = new StringBuilder();
			while (j < stationMap.getStationCode().length()
					&& !Character.isDigit(stationMap.getStationCode().charAt(j))) {
				sb.append(stationMap.getStationCode().charAt(j++));
			}
			newCodeWithoutSeq = sb.toString();
			break;
		}
		int newCodeSeq = Integer.valueOf(stationMap.getStationCode().substring(j));
		if (currCodeWithoutSeq.equalsIgnoreCase(newCodeWithoutSeq))
			return (currCodeSeq - newCodeSeq);
		return currCodeWithoutSeq.compareTo(newCodeWithoutSeq);
	}

}
