package com.example.collegeems.dao;

import com.example.collegeems.entity.Notice;
import com.example.collegeems.entity.Params;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeDao {

    List<Notice> findBySearch(@Param("params") Params params);

    List<Notice> findTop5();

    void save(Notice notice);

    void delete(Long id);

    void update(Notice notice);
}
