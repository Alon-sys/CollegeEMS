package com.example.collegeems.service;


import cn.hutool.crypto.digest.BCrypt;
import com.example.collegeems.common.BCryptExample;
import com.example.collegeems.dao.AdminDao;
import com.example.collegeems.entity.Params;
import com.example.collegeems.entity.User;
import com.example.collegeems.exception.CustomException;
import com.example.collegeems.util.JwtTokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    //注入dao层
    @Resource
    private AdminDao adminDao;




    //分页查询
    public PageInfo<User> findBySearch(Params params) {
        //开启分页查询
        //查询会自动查询分页数据
        PageHelper.startPage(params.getPageNum(),params.getPageSize());

        List<User> list = adminDao.findBySearch(params);

        return PageInfo.of(list);
    }

    //添加用户
    public void add(User admin) {
        //进行非空判断
        if (admin.getUsername()==null|| admin.getUsername().isEmpty()){
            throw new CustomException("用户名不能为空!");
        }
        //进行重复性判断
        User user = adminDao.findByName(admin.getUsername());
        if(user!=null)
        {
            throw new CustomException("该用户名已存在!");
        }
        if (admin.getEmail()!=null){
            User userEmail = adminDao.findByEmail(admin.getEmail());
            if (userEmail!=null){
                throw new CustomException("该邮箱已存在!");
            }
        }
        //初始化一个密码
        if (admin.getPassword()==null || admin.getPassword().isEmpty()){
            String rawPassword = "123456"; // 默认密码
            String encodedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
           admin.setPassword(encodedPassword);
        }else {

            admin.setPassword(BCryptExample.hashPassword(admin.getPassword()));
        }
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        adminDao.insertUser(admin);
    }

    //修改用户
    public void update(User admin) {
        admin.setUpdatedAt(LocalDateTime.now());

        admin.setPassword(BCryptExample.hashPassword(admin.getPassword()));
        adminDao.updateUser(admin);

    }

    //删除用户
    public void delete(Long id) {
        adminDao.deleteUserById(id);
    }

    //登录
    public User login(User admin) {
        //1.进行一些非空判断
        if (admin.getUsername()==null||admin.getPassword()==null){
           throw new CustomException("用户名或密码不能为空!");
        }
        if (admin.getUsername().isEmpty()||admin.getPassword().isEmpty())
        {
            throw new CustomException("用户名或密码不能为空!");
        }
        //2.从数据库中根据用户名和密码去查询用户信息
        User user = adminDao.findByName(admin.getUsername());
        if (user==null){
            throw new CustomException("用户名或密码错误!");
        }
        //3.使用 BCryptExample 类的 checkPassword 方法验证密码
        boolean isPasswordValid = BCryptExample.checkPassword(admin.getPassword(), user.getPassword());
        if (!isPasswordValid) {
            throw new CustomException("用户名或密码错误!");
        }
        //4.如果查询到了用户信息，返回用户信息
        String token=JwtTokenUtils.genToken(user.getId().toString(),user.getPassword());
        user.setToken(token);
        return user;
    }

    //根据id查询用户
    public User findById(Long id)
        {
            return adminDao.selectUserById(id);
        }

        //修改密码
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        // 1. 查询用户
        User user = findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 验证旧密码
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            return false;
        }

        // 3. 检查新旧密码是否相同
        if (BCrypt.checkpw(newPassword, user.getPassword())) {
            throw new RuntimeException("新密码不能与旧密码相同");
        }

        // 4. 加密新密码
        String encodedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // 5. 更新密码
        user.setPassword(encodedNewPassword);
        user.setUpdatedAt(LocalDateTime.now());
        adminDao.updateUser(user);

        return true;
    }

}
