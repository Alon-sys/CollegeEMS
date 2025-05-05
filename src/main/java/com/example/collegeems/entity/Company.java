package com.example.collegeems.entity;

import lombok.Data;

import javax.persistence.*;

// 公司实体
@Entity
@Table(name = "companies")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "logo")
    private String logo;
    @Column(name = "website")
    private String website;
    @Column(name = "description")
    private String description;
    @Column(name = "industry")
    private String industry;
    @Column(name = "size")
    private String size;

}
