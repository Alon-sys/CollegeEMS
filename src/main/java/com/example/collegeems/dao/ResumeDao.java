package com.example.collegeems.dao;

import com.example.collegeems.entity.Resume;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeDao {

    void save(Resume resume);

    Resume findById(Long id);

    void update(Resume resume);

    void delete(Long id);

    List<Resume> findAll();
}
