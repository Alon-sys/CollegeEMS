package com.example.collegeems.service;


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
        if (admin.getPassword()==null){
            admin.setPassword("123456");
        }
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        adminDao.insertUser(admin);


    }

    //修改用户
    public void update(User admin) {
        admin.setUpdatedAt(LocalDateTime.now());
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
        User user = adminDao.findByNameAndByPassword(admin.getUsername(), admin.getPassword());
        if (user==null){
            throw new CustomException("用户名或密码错误!");
        }
        //3.如果查询到了用户信息，返回用户信息
        String token=JwtTokenUtils.genToken(user.getId().toString(),user.getPassword());
        user.setToken(token);
        return user;
    }

    //根据id查询用户
    public User findById(Long id)
        {
            return adminDao.selectUserById(id);
        }

}
