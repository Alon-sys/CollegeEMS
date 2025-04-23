package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.Application;
import com.example.collegeems.service.ApplicationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;

    // 获取所有申请
    @GetMapping
    public Result getAllApplications() {
             List<Application> list = applicationService.getAllApplications();
        return Result.success(list);
    }

    // 添加一个申请
    @PostMapping
    public Result saveApplication(@RequestBody Application application) {
        Application save = applicationService.saveApplication(application);
        return Result.success(save);
    }
}
