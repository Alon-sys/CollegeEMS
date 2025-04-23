package com.example.collegeems.service;

import com.example.collegeems.dao.ApplicationDao;
import com.example.collegeems.entity.Application;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

// 投递记录服务
@Service
public class ApplicationService {

    @Resource
    private ApplicationDao applicationDao;

    public List<Application> getAllApplications() {
        return applicationDao.findAll();
    }

    public Application saveApplication(Application application) {
        return applicationDao.save(application);
    }
}
