package com.forest.pojo;

public class Tree {
	private String historyId;
	private String uniqueId;
	private String treeNum;
	private Float locationX;
	private Float locationY;
	private Integer isDead;
	private String area;
	private String stand;
	private String plot;
	private String treeType;
	public String getHistoryId() {
		return historyId;
	}
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getTreeNum() {
		return treeNum;
	}
	public void setTreeNum(String treeNum) {
		this.treeNum = treeNum;
	}
	public Float getLocationX() {
		return locationX;
	}
	public void setLocationX(Float locationX) {
		this.locationX = locationX;
	}
	public Float getLocationY() {
		return locationY;
	}
	public void setLocationY(Float locationY) {
		this.locationY = locationY;
	}
	
	public Integer getIsDead() {
		return isDead;
	}
	public void setIsDead(Integer isDead) {
		this.isDead = isDead;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getStand() {
		return stand;
	}
	public void setStand(String stand) {
		this.stand = stand;
	}
	public String getPlot() {
		return plot;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public String getTreeType() {
		return treeType;
	}
	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}
	public Tree(String historyId, String uniqueId, String treeNum, Float locationX, Float locationY, Integer isDead,
			String area, String stand, String plot, String treeType) {
		super();
		this.historyId = historyId;
		this.uniqueId = uniqueId;
		this.treeNum = treeNum;
		this.locationX = locationX;
		this.locationY = locationY;
		this.isDead = isDead;
		this.area = area;
		this.stand = stand;
		this.plot = plot;
		this.treeType = treeType;
	}
	public Tree() {
		super();
	}
	@Override
	public String toString() {
		return "Tree [historyId=" + historyId + ", uniqueId=" + uniqueId + ", treeNum=" + treeNum + ", locationX="
				+ locationX + ", locationY=" + locationY + ", isDead=" + isDead + ", area=" + area + ", stand=" + stand
				+ ", plot=" + plot + ", treeType=" + treeType + "]";
	}
	
	
}
