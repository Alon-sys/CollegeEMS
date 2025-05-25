package com.example.collegeems.common;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptExample {
    // 工作因子(成本)，值越高加密越安全但耗时越长
    private static final int WORKLOAD = 12;

    //  生成哈希值
    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(password, salt);
    }

    //  验证密码
    public static boolean checkPassword(String plaintext, String hashed) {
        if(hashed == null || !hashed.startsWith("$2a$")) {
            throw new IllegalArgumentException("无效的哈希值");
        }
        return BCrypt.checkpw(plaintext, hashed);
    }
}