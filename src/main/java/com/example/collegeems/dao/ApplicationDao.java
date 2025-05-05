package com.example.collegeems.dao;

import com.example.collegeems.entity.Application;
import com.example.collegeems.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationDao {
    // 保存申请
    void save(Application application);

    // 删除申请
    void delete(Long id);

    // 更新申请状态
    void update(Application application);

    // 条件查询申请
    List<Application> findBySearch(@Param("params") Params params);

    // 根据ID查询申请
    Application findById(Long id);

    // 检查用户是否已申请某职位
    boolean existsByUserIdAndJobId(@Param("userId") Long userId,
                                   @Param("jobId") Long jobId);

    // 根据用户ID查询所有申请（新增方法）
    List<Application> findByUserId(@Param("userId") Long userId);
}