package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.Params;
import com.example.collegeems.entity.Resume;
import com.example.collegeems.service.ResumeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/resume")
public class ResumeController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    // 注入服务
    @Resource
    private ResumeService resumeService;

    // 分页查询
    @GetMapping("/search")
    public Result findBySearch(Params params) {
        try {
            if (params == null) {
                throw new IllegalArgumentException("查询参数不能为空");
            }
            PageInfo<Resume> info = resumeService.findBySearch(params);
            return Result.success(info);
        } catch (Exception e) {
            logger.error("分页查询简历时出错", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "分页查询简历时出错", e);
        }
    }

    // 根据id查询
    @GetMapping("/{id}")
    public Result getResumeById(@PathVariable Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("简历ID不能为空");
            }
            Resume resume = resumeService.getById(id);
            if (resume == null) {
                return Result.error("未找到该简历");
            }
            return Result.success(resume);
        } catch (Exception e) {
            logger.error("根据ID查询简历时出错", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "根据ID查询简历时出错", e);
        }
    }

    // 添加或修改
    @PostMapping
    public Result save(@RequestBody Resume resume) {
        try {
            if (resume == null) {
                throw new IllegalArgumentException("简历对象不能为空");
            }
            if (resume.getId() == null) {
                resumeService.save(resume);
            } else {
                resumeService.update(resume);
            }
            return Result.success(resume);
        } catch (Exception e) {
            logger.error("保存或更新简历时出错", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "保存或更新简历时出错", e);
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("简历ID不能为空");
            }
            resumeService.delete(id);
            return Result.success();
        } catch (Exception e) {
            logger.error("删除简历时出错", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "删除简历时出错", e);
        }
    }

    // 批量删除
    @PutMapping("/delBatch")
    public Result delBatch(@RequestBody List<Resume> list) {
        for (Resume resume : list) {
            resumeService.delete(resume.getId());
        }
        return Result.success();
    }

    //根据用户ID查询数据
    @GetMapping("/user/{id}")
    public Result findByUserId(@PathVariable Long id){
        return Result.success(resumeService.findByUserId(id));
    }
}