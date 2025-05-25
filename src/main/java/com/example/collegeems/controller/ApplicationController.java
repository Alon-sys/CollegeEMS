package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.Application;

import com.example.collegeems.entity.Params;
import com.example.collegeems.service.ApplicationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;



@CrossOrigin
@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;

    // 查询用户是否申请过
    @GetMapping("/check")
    public Result check(@RequestParam Long jobId,@RequestParam Long userId){
        boolean applied = applicationService.existsByUserIdAndJobId(userId, jobId);
        return Result.success(applied);
    }


    //添加申请and更新申请
    @PostMapping
    public Result save(@RequestBody Application application){
        if(application.getId()==null){
            applicationService.save(application);
        }else {
            applicationService.update(application);
        }
        return Result.success(application);
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        applicationService.delete(id);
        return Result.success();
    }

    //获取已申请职位
    @GetMapping("/{id}")
    public Result getUserId (@PathVariable Long id){
        return Result.success(applicationService.findByUserID(id));
    }

    @GetMapping("/search")
    public Result findBySearch(Params params){
        return Result.success(applicationService.findBySearch(params));
    }

    //按用户查询
    @GetMapping("/user/{id}")
    public Result findByUserId(@PathVariable Long id,Params params)
    {
        return Result.success(applicationService.findByUserId(id,params));

    }


    //按公司查询
    @GetMapping("/company/{id}")
    public Result findByCompanyId(@PathVariable Long id,  Params params){
        return Result.success(applicationService.findByCompanyUserId(id,params));
    }

}
