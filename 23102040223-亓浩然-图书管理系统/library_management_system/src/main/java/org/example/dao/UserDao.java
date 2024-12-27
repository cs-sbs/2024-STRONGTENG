package org.example.dao;

import org.example.entity.User;
import java.util.Optional;
import java.util.List;

public interface UserDao {
    void insert(User user) throws Exception;
    void update(User user) throws Exception;
    void deleteById(Long id) throws Exception;
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
}