package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.User;
import com.example.collegeems.entity.Params;
import com.example.collegeems.service.AdminService;
import com.example.collegeems.util.JwtTokenUtils;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;


    //根据id查询用户
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        User admin = adminService.findById(id);
        return Result.success(admin);
    }

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

    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody ChangePasswordDTO dto) {
        // 从token中获取当前用户
        User currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("请先登录");
        }

        // 调用服务层修改密码
        try {
            boolean success = adminService.changePassword(currentUser.getId(), dto.getOldPassword(), dto.getNewPassword());
            if (success) {
                return Result.success();
            } else {
                return Result.error("旧密码不正确");
            }
        } catch (Exception e) {
            return Result.error("修改密码失败: " + e.getMessage());
        }
    }

    // 专用的DTO类
    @Data
    static class ChangePasswordDTO {
        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
    }


}