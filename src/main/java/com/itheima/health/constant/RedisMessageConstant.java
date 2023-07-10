package com.itheima.health.constant;

public class RedisMessageConstant {
    public static final String SENDTYPE_ORDER = "_001"; // 用于缓存体检预约时发送的验证码
    public static final String SENDTYPE_LOGIN = "_002"; // 用于缓存手机号快速登录时发送的验证码
    public static final String SENDTYPE_GETPWD = "_003"; // 用于缓存找回密码时发送的验证码
}