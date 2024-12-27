package org.example.service;

import org.example.entity.User;
import org.example.entity.UserRole;

/**
 * 用户认证服务接口
 * 提供用户注册和登录功能
 */
public interface AuthService {
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param role 用户角色
     * @throws Exception 当用户名已存在或参数无效时抛出异常
     */
    void register(String username, String password, UserRole role) throws Exception;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象
     * @throws Exception 当用户不存在或密码错误时抛出异常
     */
    User login(String username, String password) throws Exception;
} 