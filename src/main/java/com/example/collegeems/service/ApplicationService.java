package com.example.collegeems.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.collegeems.dao.ApplicationDao;
import com.example.collegeems.dao.JobDao;
import com.example.collegeems.dao.ResumeDao;
import com.example.collegeems.dao.StudentDao;
import com.example.collegeems.entity.Application;
import com.example.collegeems.entity.Job;
import com.example.collegeems.entity.Params;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// 投递记录服务
@Service
public class ApplicationService {

    @Resource
    private ApplicationDao applicationDao;

    @Resource
    private ResumeDao resumeDao;
    @Resource
    private JobDao jobDao;
    @Resource
    private StudentDao studentDao;

    // 保存
    public void save(Application application )
    {
        application.setAppliedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());
        applicationDao.save(application);

    }

    // 删除
    public void delete(Long id)
    {
        applicationDao.delete(id);
    }

    // 更新
    public void update(Application application)
    {
        application.setUpdatedAt(LocalDateTime.now());
        applicationDao.update(application);
    }


    //按条件查询
    public PageInfo<Application> findBySearch(Params params) {
        //开启分页查询
        //查询会自动查询分页数据
        PageHelper.startPage(params.getPageNum(),params.getPageSize());

        List<Application> list = applicationDao.findBySearch(params);
        /*for (Application application : list) {
            if (ObjectUtil.isAllEmpty(application.getJobId())){
                Job job = jobDao.findById(application.getJobId());
                if (ObjectUtil.isAllEmpty(job)){
                    application.setJobTitle(job.getTitle());
                }

            }

        }*/

        return PageInfo.of(list);
    }

    // 按id查询
    public Application findByUserID(Long id)
    {
        return applicationDao.findById(id);
    }

    public boolean existsByUserIdAndJobId(Long userId, Long jobId) {
        return applicationDao.existsByUserIdAndJobId(userId, jobId);
    }

}
