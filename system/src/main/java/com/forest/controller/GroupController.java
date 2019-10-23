package com.forest.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forest.common.ResponseResult;
import com.forest.service.GroupService;
import com.forest.utils.PageUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

@Controller
@RequestMapping("/group")
public class GroupController {
	@Autowired
	private GroupService groupService;
	
	private Gson gson = new Gson();
	private JsonParser parse =new JsonParser();
	private static final Logger logger = LoggerFactory.getLogger(PlotController.class);
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getGroupList(@RequestParam Map<String,Object> params,@RequestParam String _search,
			@RequestParam(required=false) String filters){
		logger.debug("{getGroupList},{}",params.toString());
		PageUtil pageUtil = new PageUtil(params);
		return new ResponseResult().success("success",groupService.getGroupPage(pageUtil));
    }
	
	@RequestMapping(value="/getGroupMembers",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getGroupMembers(@RequestParam String email){
		logger.debug("{getGroupMembers},{}",email);
		return new ResponseResult().success("success",groupService.getGroupMembersPage(email));
    }
	
	@RequestMapping(value="/addGroup",method=RequestMethod.POST )
	@ResponseBody
	ResponseResult addGroup(@RequestParam String email,@RequestParam String name){
		logger.debug("{addGroup},{},{}",email,name);
		int addResult = groupService.addGroup(email,name);
		if(addResult == 0)
			return new ResponseResult().failure("组长已经加入某个小组");
		else if(addResult == 1)
			return new ResponseResult().failure("小组名称重复");
		else if (addResult == 2) 
			return new ResponseResult().failure("该邮箱尚未注册");
		else if (addResult == 3)
			return new ResponseResult().failure("组员角色不可以设置为组长");
		return new ResponseResult().success("success");
    }
	
	@RequestMapping(value="/addMember",method=RequestMethod.POST )
	@ResponseBody
	ResponseResult addMember(@RequestParam String leader,@RequestParam String member){
		logger.debug("{addMember},{},{}",leader,member);
		int addResult = groupService.addMember(leader,member);
		if(addResult == 0)
			return new ResponseResult().failure("该邮箱尚未注册");
		else if (addResult == 1)
			return new ResponseResult().failure("该组员已在该小组");
		return new ResponseResult().success("success");
    }
	
	@RequestMapping(value="/deleteGroup",method=RequestMethod.POST )
	@ResponseBody
	ResponseResult deleteGroup(@RequestParam String email){
		logger.debug("{deleteGroup},{}",email);
		groupService.deleteGroup(email);
		return new ResponseResult().success("success");
		
    }
	
	@RequestMapping(value="/deleteGroupMember",method=RequestMethod.POST )
	@ResponseBody
	ResponseResult deleteGroupMember(@RequestParam String infString){
		logger.debug("{deleteGroupMember},{}",infString);
		String leader = infString.split("[*]")[0];
		String member = infString.split("[*]")[1];
		return new ResponseResult().success("success",groupService.deleteMember(leader, member));
		
    }
}
