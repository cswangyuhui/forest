package com.forest.pojo;

public class Permission {
	private String role;
	private String permission;
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Permission(String role, String permission) {
		super();
		this.role = role;
		this.permission = permission;
	}
	public Permission() {
		super();
	}
}
