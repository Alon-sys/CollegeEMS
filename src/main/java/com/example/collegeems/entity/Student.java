package com.example.collegeems.entity;

import javax.persistence.*;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "students")
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private String userID;
    @Column(name = "student_id")
    private String studentID;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private String age;
    @Column(name = "gender")
    private String gender;
    @Column(name = "college")
    private String college;
    @Column(name = "major")
    private String major;
    @Column(name = "class_info")
    private String classInfo;
    @Column(name = "is_employed")
    private String isEmployed;
}
