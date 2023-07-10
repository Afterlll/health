package com.itheima.health.service;

import com.itheima.health.entity.Result;
import com.itheima.health.pojo.User;

public interface UserService {
    Result register(User registerUser);

    Result login(User loginUser);

    User findByTelephone(String telephone);
}
