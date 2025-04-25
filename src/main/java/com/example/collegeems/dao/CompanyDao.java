package com.example.collegeems.dao;

import com.example.collegeems.entity.Company;
import com.example.collegeems.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDao {

    // 添加
    void save(Company company);
    // 修改
    void update(Company company);
    // 删除
    void delete(Long id);
    // 根据条件查询
    List<Company> findBySearch(@Param("Params") Params params);
    //根据用户iD查询数据
    Company findByUserID(Long id);

}
