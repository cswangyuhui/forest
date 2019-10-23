package com.forest.serviceImpl;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.mapper.UserMapper;
import com.forest.pojo.User;
import com.forest.pojo.UserExample;
import com.forest.pojo.UserExample.Criteria;
import com.forest.service.UserService;
import com.forest.utils.NumberUtil;
import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;
import com.forest.utils.SystemUtil;
import org.slf4j.Logger;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired
	private UserMapper userMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class); 


	public User updateTokenAndLogin(String email, String password) {
		UserExample ue = new UserExample();
		Criteria criteria = ue.createCriteria();
		criteria.andEmailEqualTo(email);
		criteria.andPasswordEqualTo(password);
		List<User> users = userMapper.selectByExample(ue);
		logger.debug("updateTokenAndLogin:{} ",users.size());
		if(users != null && users.size() == 1)
		{
			User adminUser = users.get(0);
			String token = getNewToken(System.currentTimeMillis()+"", adminUser.getName());
			if(userMapper.updateUserToken(email, token) > 0)
			{
				adminUser.setUserToken(token);
				return adminUser;
			}
		}
		return null;
	}
	
	/**
     * 获取token值
     *
     * @param sessionId
     * @param userId
     * @return
     */
    private String getNewToken(String sessionId, String userName) {
        String src = sessionId + userName + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

	public User getUserByToken(String userToken) {
		return userMapper.getUserByToken(userToken);
	}

	public int addNewUser(User user) {
		User searchUser = userMapper.selectByPrimaryKey(user.getEmail());
		if(searchUser != null) {
			return 0;
		}
		UserExample ue = new UserExample();
		Criteria criteria = ue.createCriteria();
		criteria.andNameEqualTo(user.getName());
		List<User> list = userMapper.selectByExample(ue);
		if(list != null && list.size() > 0) {
			return -1;
		}
		return userMapper.insert(user);
	}

	@Override
	public PageResult getUserPage(PageUtil pageUtil) {
		List<User> users = userMapper.findUser(pageUtil);
		int total = userMapper.getTotalUser();
		PageResult pageResult = new PageResult(users, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public int changeUserRole(String email, String role) {
		logger.debug("userService-changeUserRole,{},{}",email,role);
		return userMapper.changeUserRole(email,role);
	}
	
	@Override
	public int deleteUserByEmail(String email) {
		logger.debug("userService-deleteUserByEmail,{}",email);
		return userMapper.deleteByPrimaryKey(email);
	}

	@Override
	public int renewPassword(String oldPassword, String newPassword, String username) {
		User user = userMapper.getUserByName(username);
		if(user == null) return 2;
		if(!user.getPassword().equals(oldPassword)) return 0;
		userMapper.updateUserByName(username,newPassword);
		return 1;
	}
	
	
}
