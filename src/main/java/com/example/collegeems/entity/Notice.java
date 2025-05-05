package com.example.collegeems.entity;


import lombok.Data;
import javax.persistence.*;

@Data
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "time")
    private String time;
}