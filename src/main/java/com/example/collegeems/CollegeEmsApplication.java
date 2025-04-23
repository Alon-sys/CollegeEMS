package com.example.collegeems;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.collegeems.dao")
public class CollegeEmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollegeEmsApplication.class, args);
    }
}
