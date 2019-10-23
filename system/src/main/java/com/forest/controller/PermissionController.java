package com.forest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forest.common.ResponseResult;
import com.forest.service.PermissionService;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
@RequestMapping("/permission")
public class PermissionController {
	@Autowired
	private PermissionService permissionService;
	
	private Gson gson = new Gson();
	private JsonParser parse =new JsonParser();
	private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getPlotList(){
		return new ResponseResult().success("success",permissionService.getAllPermissions());
    }
	
	@RequestMapping(value="/renew",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult renewPermission(){
		return new ResponseResult().success("success",permissionService.renewPermission());
    }
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult deletePermission(@RequestParam String psString){
		logger.debug("permissionString,{}",psString);
		String permissionString = "";
		if(psString.startsWith("group_member"))
		{
			permissionString = psString.substring(13, psString.length());
			permissionService.deletePermission("group_member",permissionString);
		}
		else if(psString.startsWith("group_leader"))
		{
			permissionString = psString.substring(13, psString.length());
			permissionService.deletePermission("group_leader",permissionString);
		}
		else {
			permissionString = psString.substring(14, psString.length());
			permissionService.deletePermission("Administrator",permissionString);
		}
		logger.debug(permissionString);
		return new ResponseResult().success("success","删除权限成功");
    }
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult addPermission(@RequestParam String psString){
		logger.debug("permissionString,{}",psString);
		String permissionString = "";
		if(psString.startsWith("group_member"))
		{
			permissionString = psString.substring(13, psString.length());
			permissionService.insertPermission("group_member",permissionString);
		}
		else if(psString.startsWith("group_leader"))
		{
			permissionString = psString.substring(13, psString.length());
			permissionService.insertPermission("group_leader",permissionString);
		}
		else {
			permissionString = psString.substring(14, psString.length());
			permissionService.insertPermission("Administrator",permissionString);
		}
		logger.debug(permissionString);
		return new ResponseResult().success("success","增加权限成功");
    }
	
}