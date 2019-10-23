package com.forest.pojo;

public class Group {
	private String teamLeaderNumber;
	private String teamName;
	private String leaderName;
	private String leaderRole;
	
	public String getLeaderRole() {
		return leaderRole;
	}
	public void setLeaderRole(String leaderRole) {
		this.leaderRole = leaderRole;
	}
	public String getTeamLeaderNumber() {
		return teamLeaderNumber;
	}
	public void setTeamLeaderNumber(String teamLeaderNumber) {
		this.teamLeaderNumber = teamLeaderNumber;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public Group(String teamLeaderNumber, String teamName, String leaderName,String leaderRole) {
		super();
		this.teamLeaderNumber = teamLeaderNumber;
		this.teamName = teamName;
		this.leaderName = leaderName;
		this.leaderRole = leaderRole;
	}
	public Group() {
		super();
	}
	
}
