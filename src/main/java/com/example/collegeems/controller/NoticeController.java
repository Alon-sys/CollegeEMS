package com.example.collegeems.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.collegeems.common.Result;
import com.example.collegeems.entity.Job;
import com.example.collegeems.entity.Notice;
import com.example.collegeems.entity.Params;
import com.example.collegeems.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    //按条件查询
    @GetMapping("/search")
    public Result findBySearch(Params params){
        PageInfo<Notice> info = noticeService.findBySearch(params);
        return Result.success(info);
    }

    @PostMapping
    public Result save(@RequestBody Notice notice) {
        if (ObjectUtil.isEmpty(notice.getId())) {
            noticeService.save(notice);
        }  else {
            noticeService.update(notice);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        noticeService.delete(id);
        return Result.success();
    }

    @GetMapping
    public Result findTop( ){
        List<Notice> list = noticeService.findTop();
        return Result.success(list);
    }

}
