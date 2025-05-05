package com.example.collegeems.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

// 简历实体类
@Data
@Entity
@Table(name = "resumes")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "content")
    private String content;
    @Column(name = "resume_photo")
    private String resumePhoto;
    @Column(name = "created_at")
    private LocalDateTime  createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "name")
    private String name;
    @Column(name = "gender")
    private String gender;
    @Column(name = "birthday")
    private java.sql.Date birthday;
    @Column(name = "education")
    private String education;
    @Column(name = "school")
    private String school;
    @Column(name = "major")
    private String major;
    @Column(name = "skills")
    private String skills;
    @Column(name = "experience")
    private String experience;
    @Column(name = "projects")
    private String projects;
    @Column(name = "resume_url")
    private String resumeUrl;

    /**
     * 实体创建前的回调方法，在创建用户记录时自动设置创建时间和更新时间为当前时间。
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 实体更新前的回调方法，在更新用户记录时自动设置更新时间为当前时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
