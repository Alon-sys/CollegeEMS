package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.Job;
import com.example.collegeems.entity.Params;
import com.example.collegeems.service.JobService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/jobs")
public class JobController {

    @Resource
    private JobService jobService;

    // 增加职位and更新公司职位信息
    @PostMapping
    public Result save(@RequestBody Job job){
        if(job.getId()==null){
            jobService.save(job);
        }else {
           jobService.update(job);
        }
        return Result.success(job);
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        jobService.delete(id);
        return Result.success();
    }


    //按条件查询
    @GetMapping("/search")
    public Result findBySearch(Params params){
        PageInfo<Job> info = jobService.findBySearch(params);
        return Result.success(info);
    }

    //按id查询
    @GetMapping("/{id}")
    public Result findByUserId(@PathVariable Long id){
        return Result.success(jobService.findByUserId(id));
    }
}
