package com.forest.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.forest.pojo.Group;
import com.forest.pojo.User;
import com.forest.utils.PageUtil;

public interface GroupMapper {

	List<Group> findGroup(Map param);

	int getTotalGroup();

	List<User> getMembersOfGroup(String email);

	int addGroup(@Param("email") String email, @Param("name") String name,
                 @Param("username") String username, @Param("role") String role);
	
	int deleteGroup(@Param("email") String email);

	int deleteMember(@Param("leader") String leader, @Param("member") String member);

	int clearMembers(@Param("email") String email);

	int addMember(@Param("leader") String leader, @Param("member") String member);
	
	List<String> findLeaderNumber(@Param("member") String member);

}
