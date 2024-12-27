package org.example.service.impl;

import org.example.dao.UserDao;
import org.example.dao.impl.UserDaoImpl;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.service.AuthService;

public class AuthServiceImpl implements AuthService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void register(String username, String password, UserRole role) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("密码不能为空且长度必须大于6位");
        }
        if (role == null) {
            throw new IllegalArgumentException("用户角色不能为空");
        }

        // 检查用户名是否已存在
        if (userDao.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 实际项目中应该加密存储
        user.setRole(role);

        userDao.insert(user);
    }

    @Override
    public User login(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new Exception("用户不存在"));
        
        if (!user.getPassword().equals(password)) { // 实际项目中应该比较加密后的密码
            throw new Exception("密码错误");
        }
        
        return user;
    }
} 