package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.Params;
import com.example.collegeems.entity.Resume;
import com.example.collegeems.entity.Student;
import com.example.collegeems.service.ResumeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/resume")
public class ResumeController {

    // 注入服务
    @Resource
    private ResumeService resumeService;


    // 分页查询
    @GetMapping("/search")
    public Result findBySearch(Params params){
        PageInfo<Resume> info = resumeService.findBySearch(params);
        return Result.success(info);
    }


    // 添加或修改
    @PostMapping
    public Result save(@RequestBody Resume resume){
        if(resume.getId()==null){
            resumeService.save(resume);
        }else {
           resumeService.update(resume);
        }
        return Result.success(resume);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        resumeService.delete(id);
        return Result.success();
    }


}
