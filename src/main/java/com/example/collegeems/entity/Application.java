package com.example.collegeems.entity;


import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;


@Table(name = "applications")
@Data
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "job_id")
    private Long jobId;
    @Column(name = "resume_id")
    private Long resumeId;
    @Column(name = "status")
    private String status;
    @Column(name = "applied_at")
    private LocalDateTime appliedAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private String studentName;
    @Transient
    private String jobTitle;

    /**
     * 实体创建前的回调方法，在创建用户记录时自动设置创建时间和更新时间为当前时间。
     */
    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
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
