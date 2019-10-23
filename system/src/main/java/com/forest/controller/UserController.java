package com.forest.controller;


import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forest.annotation.TokenToUser;
import com.forest.common.ResponseResult;
import com.forest.pojo.User;
import com.forest.service.UserService;
import com.forest.utils.PageUtil;

import org.slf4j.Logger;




@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value="/addUser",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult addUser(@RequestParam String email,@RequestParam String name,@RequestParam String password){
		logger.debug("register:{} ",email + " " + name + " " + password);
		User user = new User(email,password,name,null,new Timestamp(System.currentTimeMillis()),"group_member");
		int result = userService.addNewUser(user);
		if(result == 0) return new ResponseResult().failure("账号已被注册");
		else if(result == -1) return new ResponseResult().failure("用户名已被注册");
		else return new ResponseResult().success("注册成功");
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
    public ResponseResult login(@RequestParam String email,@RequestParam String password) {
        
		logger.debug("login:{} ",email+" "+password);
		ResponseResult rr = new ResponseResult().failure("登录失败!");
		
        User loginUser = userService.updateTokenAndLogin(email, password);
        if (loginUser != null) {
            rr = new ResponseResult().success("登录成功!",loginUser);
        }
        return rr;
    }
	@RequestMapping(value = "/tokenToName", method = RequestMethod.GET)
	@ResponseBody
    public ResponseResult test1(@TokenToUser User user) {
        //logger.info("haha");
		ResponseResult result = null;
        if (user == null) {
            //如果通过请求header中的token未查询到用户的话即token无效，登陆验证失败，返回未登录错误码。
            result = new ResponseResult().failure("token无效!登陆验证失败!");
        } else {
            //登陆验证通过。
            result = new ResponseResult().success("登陆验证成功!",user.getName());
        }
        return result;
    }
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getUserList(@RequestParam Map<String,Object> params,@RequestParam String _search,
			@RequestParam(required=false) String filters){
		logger.debug("{getUserList},{}",params.toString());
		PageUtil pageUtil = new PageUtil(params);
		return new ResponseResult().success("success",userService.getUserPage(pageUtil));
    }
	
	@RequestMapping(value="/changeUserRole",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult changeUserRole(@RequestParam String email,@RequestParam String role){
		logger.debug("{changeUserRole},{},{}",email,role);
		userService.changeUserRole(email,role);
		return new ResponseResult().success("success","更新用户角色成功");
    }
	
	@RequestMapping(value="/renewPassword",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult renewPassword(@RequestParam String oldPassword,@RequestParam String newPassword,@RequestParam String username){
		logger.debug("{renewPassword},{},{},{}",oldPassword,newPassword,username);
		int result = userService.renewPassword(oldPassword,newPassword,username);
		if(result == 0) return new ResponseResult().failure("旧密码错误");
		if(result == 2) return new ResponseResult().failure("未找到该用户");
		return new ResponseResult().success("success","更新用户密码成功");
    }
	
	@RequestMapping(value="/deleteUserByEmail",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult deleteUserByEmail(@RequestParam String email){
		logger.debug("{deleteUserByEmail},{}",email);
		userService.deleteUserByEmail(email);
		return new ResponseResult().success("success","删除用户成功");
    }
}
