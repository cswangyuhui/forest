package com.forest.mapper;

import com.forest.pojo.User;
import com.forest.pojo.UserExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(String email);

    int insert(User record);
    
    int insertNewUser(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(String email);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    /**
     * 更新用户token值
     *
     * @param userId
     * @param newToken
     * @return
     */
    int updateUserToken(@Param("userEmail") String email, @Param("newToken") String newToken);
    
    User getUserByToken(String userToken);
    
    List<User> findUser(Map param);
    
    int getTotalUser();

	int changeUserRole(@Param("email") String email, @Param("role") String role);

	User getUserByName(@Param("username") String username);

	int updateUserByName(@Param("username") String username, @Param("newPassword") String newPassword);
}