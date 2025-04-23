package com.example.collegeems.dao;

import com.example.collegeems.entity.Params;
import com.example.collegeems.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao {

    //新增
    void save( Student student);

    void delete(Long id);

    void update( Student student);

    // 根据条件查询
    List<Student> findBySearch(@Param("Params") Params params);


}
