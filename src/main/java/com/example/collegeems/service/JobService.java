package com.example.collegeems.service;

import com.example.collegeems.dao.JobDao;
import com.example.collegeems.entity.Job;
import com.example.collegeems.entity.Params;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    @Resource
    private JobDao jobDao;

    public void save(Job job) {
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        jobDao.save(job);

    }

    public void delete(Long id) {
        jobDao.delete(id);

    }

    public void update(Job job) {
        job.setUpdatedAt(LocalDateTime.now());
        jobDao.update(job);

    }

    //按条件查询
    public PageInfo<Job> findBySearch(Params params) {
        //开启分页查询
        //查询会自动查询分页数据
        PageHelper.startPage(params.getPageNum(),params.getPageSize());

        List<Job> list = jobDao.findBySearch(params);

        return PageInfo.of(list);


    }
    //按公司id查询
    public List<Job> findByUserId(Long id) {
        return jobDao.findByUserId(id);
    }
}
