package com.example.collegeems.controller;

import com.example.collegeems.common.Result;
import com.example.collegeems.service.ResumeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("/resumes")
public class ResumeController {

    @Resource
    private ResumeService resumeService;

    public Result getAllResumes() {
        return null;
    }
}
