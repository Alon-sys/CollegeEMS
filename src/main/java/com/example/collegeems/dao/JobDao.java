package com.example.collegeems.dao;

import com.example.collegeems.entity.Job;
import com.example.collegeems.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobDao {

    // 根据id查询职位
    Job findById(Long id);
    // 保存职位
    void save(Job job);
    // 更新职位
    void update(Job job);
    // 删除职位
    void delete(Long id);
    // 根据搜索条件查询职位
    List<Job> findBySearch(@Param("Params") Params params);

}
