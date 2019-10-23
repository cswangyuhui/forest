package com.forest.service;

import com.forest.pojo.User;
import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;

public interface UserService {
	 /**
     * 登陆功能
     *
     * @return
     */
    User updateTokenAndLogin(String email, String password);

    /**
     * 根据userToken获取用户记录
     *
     * @return
     */
    User getUserByToken(String userToken);
    
    int addNewUser(User user);

	PageResult getUserPage(PageUtil pageUtil);

	int changeUserRole(String email, String role);
	
	int deleteUserByEmail(String email);

	int renewPassword(String oldPassword, String newPassword, String username);
}
