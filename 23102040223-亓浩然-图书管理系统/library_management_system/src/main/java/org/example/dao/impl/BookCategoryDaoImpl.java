package org.example.dao.impl;

import org.example.dao.BookCategoryDao;
import org.example.entity.BookCategory;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCategoryDaoImpl implements BookCategoryDao {
    
    @Override
    public void insert(BookCategory category) {
        String sql;
        if (category.getId() != null) {
            // 如果有ID，使用REPLACE INTO
            sql = "REPLACE INTO book_categories (id, name) VALUES (?, ?)";
        } else {
            // 如果没有ID，使用普通INSERT
            sql = "INSERT INTO book_categories (name) VALUES (?)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (category.getId() != null) {
                ps.setLong(1, category.getId());
                ps.setString(2, category.getName());
            } else {
                ps.setString(1, category.getName());
            }
            
            ps.executeUpdate();
            
            // 如果没有指定ID，从生成的键中获取ID
            if (category.getId() == null) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setId(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("插入图书分类失败", e);
        }
    }

    @Override
    public void update(BookCategory category) {
        String sql = "UPDATE book_categories SET name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setLong(2, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("更新图书分类失败", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM book_categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("删除图书分类失败", e);
        }
    }

    @Override
    public Optional<BookCategory> findById(Long id) {
        String sql = "SELECT * FROM book_categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BookCategory category = new BookCategory();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                return Optional.of(category);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("查询图书分类失败", e);
        }
    }

    @Override
    public Optional<BookCategory> findByName(String name) {
        String sql = "SELECT * FROM book_categories WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BookCategory category = new BookCategory();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                return Optional.of(category);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("查询图书分类失败", e);
        }
    }

    @Override
    public List<BookCategory> findAll() {
        String sql = "SELECT * FROM book_categories";
        List<BookCategory> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BookCategory category = new BookCategory();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException("查询所有图书分类失败", e);
        }
    }
} 