package com.example.collegeems.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

// 简历实体类
@Data
@Entity
@Table(name = "resumes")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String name;
    private String gender;
    private java.sql.Date birthday;
    private String education;
    private String school;
    private String major;
    private String skills;
    private String experience;
    private String projects;
}
