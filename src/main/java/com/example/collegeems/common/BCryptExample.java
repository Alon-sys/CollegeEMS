package com.example.collegeems.common;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptExample {

    // 方法：生成bcrypt加密的密码
    public static String hashPassword(String plainPassword) {
        // 使用BCrypt.getsalt()生成盐并进行加密，默认工作因子是10
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt); // 返回加密后的密码
    }

    // 方法：验证密码是否与加密后的密码匹配
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword); // 返回密码是否匹配
    }
}