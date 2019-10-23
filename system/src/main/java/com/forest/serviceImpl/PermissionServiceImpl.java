package com.forest.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.mapper.PermissionMapper;
import com.forest.pojo.Permission;
import com.forest.service.PermissionService;
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService{
	private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
	@Autowired
	private PermissionMapper permissionMapper;
	@Override
	public List<Permission> getAllPermissions() {
		return permissionMapper.getAllPermissions();
	}
	@Override
	public int deletePermission(String role, String permission) {
		logger.debug("delete: role{},permission{}",role,permission);
		return permissionMapper.deletePermission(role,permission);
	}
	@Override
	public int insertPermission(String role, String permission) {
		logger.debug("insert: role{},permission{}",role,permission);
		return permissionMapper.insertPermission(role,permission);
	}
	@Override
	public int renewPermission() {
		List<Permission> list = permissionMapper.initPermissions();
		permissionMapper.clearPermission();
		return permissionMapper.insertForeach(list);
	}
	@Override
	public List<String> getPermissionOfRole(String role) {
		return permissionMapper.getPermissionOfRole(role);
	}
	
}
