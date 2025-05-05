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
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "title")
    private String title;
    @Column(name = "salary")
    private String salary;
    @Column(name = "education")
    private String description;
    @Column(name = "createAt")
    private LocalDateTime createdAt;
    @Column(name = "updateAt")
    private LocalDateTime updatedAt;
    @Column(name = "location")
    private String location;
    @Column(name = "category")
    private String category;
    @Column(name = "type")
    private String type;
    @Column(name = "isActive")
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
     * 实体更新前的回调方法，在更新用户记录时自动设置更新时间为当前时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
