package com.example.collegeems.service;

import com.example.collegeems.dao.ResumeDao;
import com.example.collegeems.entity.Resume;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    @Resource
    private ResumeDao resumeDao;

    // 保存简历
    public void save(Resume resume) {
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
    //查询所有简历
    public List<Resume> findAll() {
        return resumeDao.findAll();
    }

    //更新简历
    public void update(Resume resume) {
        resumeDao.update(resume);
    }




}
