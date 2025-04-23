package com.example.collegeems.entity;


import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;


@Table(name = "applications")
@Data
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long jobId;
    private Long resumeId;
    private String status;
    private Timestamp appliedAt;
    private Timestamp updatedAt;
}
