package com.example.collegeems.dao;

import com.example.collegeems.entity.User;
import com.example.collegeems.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao {

    // 按用户ID查询用户
    User selectUserById(Long id);

    //添加用户
    int insertUser(User user);
    //更新用户
    int updateUser(User user);
    //按用户ID删除用户
    int deleteUserById(Long id);

    //按条件查询用户
    List<User> findBySearch(@Param("Params") Params params);

    //按用户名查询用户
    User findByName(@Param("username") String username);

    //按邮箱查询
    User findByEmail(@Param("email") String email);



}
