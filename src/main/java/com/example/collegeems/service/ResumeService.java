package com.example.collegeems.service;

import com.example.collegeems.dao.ResumeDao;
import com.example.collegeems.entity.Params;
import com.example.collegeems.entity.Resume;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResumeService {

    @Resource
    private ResumeDao resumeDao;


    // 保存简历
    public void save(Resume resume) {
        resume.setCreatedAt(LocalDateTime.now());
        resume.setUpdatedAt(LocalDateTime.now());
        resumeDao.save(resume);
    }
    //查询简历
    public Resume findById(Long id) {
        return resumeDao.findById(id);
    }
    //删除简历
    public void delete(Long id) {
        resumeDao.delete(id);
    }

    //更新简历
    public void update(Resume resume) {
        resume.setUpdatedAt(LocalDateTime.now());
        resumeDao.update(resume);
    }


    // 分页查询简历
    public PageInfo<Resume> findBySearch(Params params) {
        //开启分页查询
        //查询会自动查询分页数据
        PageHelper.startPage(params.getPageNum(),params.getPageSize());

        List<Resume> list = resumeDao.findBySearch(params);

        return PageInfo.of(list);
    }

    public Resume getById(Long id) {
        return resumeDao.findById(id);
    }

    public  List<Resume> findByUserId(Long id) {
        return resumeDao.findByUserId(id);

    }
}
