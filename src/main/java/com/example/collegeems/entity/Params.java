package com.example.collegeems.entity;

import lombok.Data;

import javax.persistence.Column;

// 分页参数
@Data
public class Params {
    private Long id;
    private String username;
    private String email;
    private Integer pageNum;
    private Integer pageSize;
    private String studentID;
    private String name;
    private String industry;
    private String size;

    private String title;
    private String location;
    private String category;
    private String type;





}
