package com.forest.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.mapper.GroupMapper;
import com.forest.mapper.UserMapper;
import com.forest.pojo.Group;
import com.forest.pojo.User;
import com.forest.service.GroupService;
import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;
@Service("groupService")
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private UserMapper userMapper;
	@Override
	public PageResult getGroupPage(PageUtil pageUtil) {
		List<Group> groups = groupMapper.findGroup(pageUtil);
		int total = groupMapper.getTotalGroup();
		PageResult pageResult = new PageResult(groups, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}
	@Override
	public PageResult getGroupMembersPage(String email) {
		List<User> members = groupMapper.getMembersOfGroup(email);
		PageResult pageResult = new PageResult(members, members.size(), 0, members.size());
		return pageResult;
	}
	@Override
	public int addGroup(String email, String name) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("email",email);
		List<Group> groups = groupMapper.findGroup(param);
		if(groups.size() != 0)
			return 0;
		param.clear();
		param.put("name",name);
		groups = groupMapper.findGroup(param);
		if(groups.size() != 0)
			return 1;
		User leader = userMapper.selectByPrimaryKey(email);
		if(leader == null)
			return 2;
		//System.out.println("###"+leader.getRole());
		if(leader.getRole().equals("group_member"))
			return 3;
		groupMapper.addGroup(email,name,leader.getName(),leader.getRole());
		return 4;
	}
	@Override
	public int deleteGroup(String email) {
		groupMapper.clearMembers(email);
		return groupMapper.deleteGroup(email);
	}
	@Override
	public int clearMembers(String email) {
		return groupMapper.clearMembers(email);
	}
	@Override
	public int deleteMember(String leader,String member) {
		return groupMapper.deleteMember(leader,member);
	}
	@Override
	public int addMember(String leader, String member) {
		List<String> leaders = groupMapper.findLeaderNumber(member);
		for(String l :leaders) {
			if(l.equals(leader))
				return 1;
		}
		User user = userMapper.selectByPrimaryKey(member);
		if(user == null)
			return 0;
		groupMapper.addMember(leader,member);
		return 2;
	}

}
