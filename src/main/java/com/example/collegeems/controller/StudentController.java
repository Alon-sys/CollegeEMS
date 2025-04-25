package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.Params;
import com.example.collegeems.entity.Student;
import com.example.collegeems.service.StudentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    //按条件查询
    @GetMapping("/search")
    public Result findBySearch(Params params){
        PageInfo<Student> info = studentService.findBySearch(params);
        return Result.success(info);
    }


    // 增加公司and更新公司信息
    @PostMapping
    public Result save(@RequestBody Student student){
        if(student.getId()==null){
            studentService.save(student);
        }else {
            studentService.update(student);
        }
        return Result.success(student);
    }
    // 删除公司
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        studentService.delete(id);
        return Result.success();
    }

   //按登录用户id查询学生信息
    @GetMapping("/{id}")
    public Result findByUserId(@PathVariable Long id){
        Student student = studentService.findByUserID(id);
        return Result.success(student);
    }


}
