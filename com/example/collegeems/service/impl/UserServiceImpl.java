package com.example.collegeems.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.collegeems.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User getUserByName(String name) {
        return getBaseMapper().selectOneByMap(Map.of("name", name));
    }
}