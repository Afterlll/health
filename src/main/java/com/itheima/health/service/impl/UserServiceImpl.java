package com.itheima.health.service.impl;

import com.itheima.health.dao.UserDAO;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Primary // 若存在多个相同类型的实现类，通过Primary可以找到
@Service
public class UserServiceImpl implements UserService {
    // 业务层依赖DAO
    @Autowired
    private UserDAO userDAO;

    @Override
    public Result register(User registerUser) {
        // 1. 调用DAO查看用户名是否已被注册
        User dbUser = userDAO.findByUsername(registerUser.getUsername());
        if (dbUser != null) {
            return new Result(false, "注册失败，用户名已存在！");
        }

        // 2. 调用DAO查看手机号是否已被注册
        dbUser = userDAO.findByTelephone(registerUser.getTelephone());
        if (dbUser != null) {
            return new Result(false, "注册失败，手机号已被注册！");
        }

        // 3. 使用BCrypt进行密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(registerUser.getPassword());
        registerUser.setPassword(encodePassword);

        // 4， 调用DAO将用户信息添加到数据库中
        userDAO.register(registerUser);

        return new Result(true, "注册成功！");
    }

    /**
     * 用户登录
     * @param loginUser
     * @return
     */
    @Override
    public Result login(User loginUser) {
        // 1. 调用DAO通过用户名查询用户
        User username = userDAO.findByUsername(loginUser.getUsername());
        if (username == null) {
            return new Result(false, "登录失败，用户名不存在！");
        }

        // 2. 用户名找到用户,对比密码
        String password = username.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(loginUser.getPassword(), username.getPassword());
        if (matches) {
            return new Result(true, "登录成功！");
        } else {
            return new Result(false, "登录失败，密码错误！");
        }
    }

    /**
     * 根据手机号查找用户
     * @param telephone
     * @return
     */
    @Override
    public User findByTelephone(String telephone) {
        return userDAO.findByTelephone(telephone);
    }
}
