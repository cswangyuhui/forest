package com.forest.pojo;

import java.sql.Timestamp;

public class DeadTree {
	private String uniqueId;
	private String note;
	private Timestamp datetime;
	private Integer number;
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Timestamp getDatetime() {
		return datetime;
	}
	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public DeadTree(String uniqueId, String note, Timestamp datetime, Integer number) {
		super();
		this.uniqueId = uniqueId;
		this.note = note;
		this.datetime = datetime;
		this.number = number;
	}
	public DeadTree() {
		super();
	}
	@Override
	public String toString() {
		return "DeadTree [uniqueId=" + uniqueId + ", note=" + note + ", datetime=" + datetime + ", number=" + number
				+ "]";
	}
	
}
