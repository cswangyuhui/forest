package com.forest.service;

import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;

public interface GroupService {

	PageResult getGroupPage(PageUtil pageUtil);
	
	PageResult getGroupMembersPage(String email);

	int addGroup(String email, String name);

	int deleteGroup(String email);

	int clearMembers(String email);
	
	int deleteMember(String leader, String member);

	int addMember(String leader, String member);
}
