package com.example.collegeems.entity;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

// 职位实体类
@Data
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long companyId;
    private String title;
    private String salary;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String location;
    private String category;
    private String type;
    private Boolean isActive;


    /**
     * 实体创建前的回调方法，在创建用户记录时自动设置创建时间和更新时间为当前时间。
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 实体更新前的回调方法，在更新用户记录时自动设置更新时间为当前时间。
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
