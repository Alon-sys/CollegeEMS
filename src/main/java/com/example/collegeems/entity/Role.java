package com.example.collegeems.entity;

import lombok.Data;

import javax.persistence.*;


// 角色表
@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
