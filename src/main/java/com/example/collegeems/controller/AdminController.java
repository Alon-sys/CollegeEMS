package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.User;
import com.example.collegeems.entity.Params;
import com.example.collegeems.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private AdminService adminService;


    //按条件查询
    @GetMapping("/search")
    public Result findBySearch(Params params){
        PageInfo<User> info = adminService.findBySearch(params);
        return Result.success(info);
    }

    //添加用户and更新用户
    @PostMapping
    public Result save(@RequestBody User user){
        if(user.getId()==null){
            adminService.add(user);
        }else {
            adminService.update(user);
        }
        return Result.success(user);
    }

    //删除用户
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        adminService.delete(id);
        return Result.success();
    }

    //登录
    @PostMapping("/login")
    public Result Login (@RequestBody User user){
    User loginUser = adminService.login(user);
        return Result.success(loginUser);
    }

    //注册
    @PostMapping("/register")
    public Result register (@RequestBody User user){
        adminService.add(user);
        return Result.success();
    }

}