package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.entity.Company;
import com.example.collegeems.entity.Params;
import com.example.collegeems.entity.User;
import com.example.collegeems.service.CompanyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/company")
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private CompanyService companyService;


    //按条件查询
    @GetMapping("/search")
    public Result findBySearch(Params params){
        PageInfo<Company> info = companyService.findBySearch(params);
        return Result.success(info);
    }


    // 增加公司and更新公司信息
    @PostMapping
    public Result save(@RequestBody Company company){
        if(company.getId()==null){
            companyService.save(company);
        }else {
            companyService.update(company);
        }
        return Result.success(company);
    }
    // 删除公司
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        companyService.delete(id);
        return Result.success();
    }

    //根据用户iD查寻公司
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        Company company = companyService.findByUserID(id);
        return Result.success(company);
    }


}
