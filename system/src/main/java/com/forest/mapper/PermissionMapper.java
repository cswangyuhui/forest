package com.forest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.forest.pojo.Permission;

public interface PermissionMapper {
	List<Permission> getAllPermissions();
	
	List<Permission> initPermissions();

	int insertForeach(List<Permission> list);
	
	int clearPermission();
	
	int deletePermission(@Param("role") String role, @Param("permission") String permission);

	int insertPermission(@Param("role") String role, @Param("permission") String permission);
	
	List<String> getPermissionOfRole(String role);
}
