package com.example.collegeems.dao;

import com.example.collegeems.entity.Application;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationDao {


    List<Application> findAll();

    Application save(Application application);
}
