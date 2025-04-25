package com.example.collegeems.service;

import com.example.collegeems.dao.StudentDao;
import com.example.collegeems.entity.Company;
import com.example.collegeems.entity.Params;
import com.example.collegeems.entity.Student;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Resource
    private StudentDao studentDao;

    // 保存
    public void save(Student student)
    {
        studentDao.save(student);
    }

    // 删除
    public void delete(Long id)
    {
        studentDao.delete(id);
    }

    // 更新
    public void update(Student student)
    {
        studentDao.update(student);
    }



    //按条件查询
    public PageInfo<Student> findBySearch(Params params) {
        //开启分页查询
        //查询会自动查询分页数据
        PageHelper.startPage(params.getPageNum(),params.getPageSize());

        List<Student> list = studentDao.findBySearch(params);

        return PageInfo.of(list);
    }

    //按用户id查询
    public Student findByUserID(Long id) {
        return studentDao.findByUserID(id);
    }

    public Student findById(Long id) {
        return studentDao.findById(id);
    }

}
