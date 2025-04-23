package com.example.collegeems.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类，用于映射数据库中的 users 表。
 * 该类包含了用户的基本信息，如用户名、密码、邮箱等。
 */

@Schema(name = "用户实体类")
@Data
@Entity
@Table(name = "users")
public class User {

    @Transient
    private String token;

    /**
     * 用户的唯一标识符，数据库中自增主键。
     */
    @Schema(description = "用户ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户登录使用的用户名，在数据库中必须唯一且不为空。
     */
    @Schema(description = "用户名", required = true)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * 用户登录的密码，在数据库中不能为空。
     */
    @Schema(description = "密码", required = true)
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 用户的邮箱地址，用于接收通知等信息，在数据库中必须唯一且不为空。
     */
    @Schema(description = "邮箱", required = true)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * 用户的角色，如管理员、学生、公司等，在数据库中不能为空。
     */
    @Schema(description = "角色", required = true)
    @Column(name = "role", nullable = false)
    private String role;

    /**
     * 用户记录创建的时间，在记录创建时自动生成，后续更新操作不会修改该值。
     */
    @Schema(description = "创建时间")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 用户记录最后一次更新的时间，每次记录更新时自动更新该值。
     */
    @Schema(description = "更新时间")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    /**
     * 用户的电话号码。
     */
    @Schema(description = "电话号码")
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 用户的真实姓名。
     */
    @Schema(description = "真实姓名")
    @Column(name = "real_name")
    private String realName;

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