package com.forest.pojo;

public class Plot {
	private String area;
	private String stand;
	private String plot;
	private Float xAxis;
	private Float yAxis;
	private String shape;
	private Double longitude;
	private Double latitude;
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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
	public Float getxAxis() {
		return xAxis;
	}
	public void setxAxis(Float xAxis) {
		this.xAxis = xAxis;
	}
	public Float getyAxis() {
		return yAxis;
	}
	public void setyAxis(Float yAxis) {
		this.yAxis = yAxis;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	@Override
	public String toString() {
		return "Plot [area=" + area + ", stand=" + stand + ", plot=" + plot + ", xAxis=" + xAxis + ", yAxis=" + yAxis
				+ ", shape=" + shape + ", getArea()=" + getArea() + ", getStand()=" + getStand() + ", getPlot()="
				+ getPlot() + ", getxAxis()=" + getxAxis() + ", getyAxis()=" + getyAxis() + ", getShape()=" + getShape()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	public Plot(String area, String stand, String plot, Float xAxis, Float yAxis, String shape) {
		super();
		this.area = area;
		this.stand = stand;
		this.plot = plot;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.shape = shape;
	}
	public Plot(String area, String stand, String plot, Float xAxis, Float yAxis, String shape,Double longitude,Double latitude) {
		super();
		this.area = area;
		this.stand = stand;
		this.plot = plot;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.shape = shape;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public Plot() {
		super();
	}
	
}
