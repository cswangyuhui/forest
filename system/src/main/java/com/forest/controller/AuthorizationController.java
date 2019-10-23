package com.forest.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forest.annotation.TokenToUser;
import com.forest.common.ResponseResult;
import com.forest.pojo.User;
import com.forest.service.PermissionService;

@Controller
@RequestMapping("/authorization")
public class AuthorizationController {
	@Autowired
	private PermissionService permissionService;
	private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
	@RequestMapping(value="/getPermission",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getUserPermission(@TokenToUser User user){
		if(user == null)
			return new ResponseResult().failure("未找到该用户");
		else {
			logger.debug("getUserPermission,{},{}",user.getName(),user.getRole());
			List<String> ps = permissionService.getPermissionOfRole(user.getRole());
			if(ps.size() == 0) return new ResponseResult().failure("该用户没有任何权限");
			Map<String,Object> map = new HashedMap<>();
			map.put("permission", ps);
			map.put("username", user.getName());
			map.put("role", user.getRole());
			map.put("email", user.getEmail());
			return new ResponseResult().success("success",map);
		}
    }

}
