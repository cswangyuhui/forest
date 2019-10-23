package com.forest.service;

import java.util.List;

import com.forest.pojo.Permission;

public interface PermissionService {
	List<Permission> getAllPermissions();
	int deletePermission(String role, String permission);
	int insertPermission(String role, String permission);
	int renewPermission();
	List<String> getPermissionOfRole(String role);
}
