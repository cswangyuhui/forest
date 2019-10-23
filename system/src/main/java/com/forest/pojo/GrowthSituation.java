package com.forest.pojo;

import java.sql.Timestamp;

public class GrowthSituation {
	private Float DBH;
	private Float height;
	private Float BLC;
	private Float CWe;
	private Float CWw;
	private Float CWs;
	private Float CWn;
	private String uniqueId;
	private Timestamp datetime;
	public GrowthSituation(Float DBH, Float height, Float bLC, Float cWe, Float cWw, Float cWs, Float cWn,
			String uniqueId, Timestamp datetime) {
		super();
		this.DBH = DBH;
		this.height = height;
		this.BLC = bLC;
		this.CWe = cWe;
		this.CWw = cWw;
		this.CWs = cWs;
		this.CWn = cWn;
		this.uniqueId = uniqueId;
		this.datetime = datetime;
	}
	public GrowthSituation() {
		super();
	}
	public Float getDBH() {
		return DBH;
	}
	public void setDBH(Float dBH) {
		DBH = dBH;
	}
	public Float getHeight() {
		return height;
	}
	public void setHeight(Float height) {
		this.height = height;
	}
	public Float getBLC() {
		return BLC;
	}
	public void setBLC(Float bLC) {
		BLC = bLC;
	}
	public Float getCWe() {
		return CWe;
	}
	public void setCWe(Float cWe) {
		CWe = cWe;
	}
	public Float getCWw() {
		return CWw;
	}
	public void setCWw(Float cWw) {
		CWw = cWw;
	}
	public Float getCWs() {
		return CWs;
	}
	public void setCWs(Float cWs) {
		CWs = cWs;
	}
	public Float getCWn() {
		return CWn;
	}
	public void setCWn(Float cWn) {
		CWn = cWn;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public Timestamp getDatetime() {
		return datetime;
	}
	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}
	@Override
	public String toString() {
		return "GrowthSituation [DBH=" + DBH + ", height=" + height + ", BLC=" + BLC + ", CWe=" + CWe + ", CWw=" + CWw
				+ ", CWs=" + CWs + ", CWn=" + CWn + ", uniqueId=" + uniqueId + ", datetime=" + datetime + "]";
	}
	
}
