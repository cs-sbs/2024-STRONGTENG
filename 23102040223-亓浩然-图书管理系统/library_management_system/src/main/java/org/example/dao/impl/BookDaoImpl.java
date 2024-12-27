package org.example.dao.impl;

import org.example.dao.BookDao;
import org.example.entity.*;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao {
    
    @Override
    public void insert(BaseBook book) throws Exception {
        book.saveToAttribute();  // 保存特有属性到attribute
        String sql = "INSERT INTO books (title, author, publication_date, isbn, category_id, attribute) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setDate(3, Date.valueOf(book.getPublicationDate()));
            ps.setString(4, book.getIsbn());
            ps.setLong(5, book.getCategoryId());
            ps.setString(6, book.getAttribute());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(BaseBook book) throws Exception {
        book.saveToAttribute();  // 保存特有属性到attribute
        String sql = "UPDATE books SET title=?, author=?, publication_date=?, isbn=?, category_id=?, attribute=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setDate(3, Date.valueOf(book.getPublicationDate()));
            ps.setString(4, book.getIsbn());
            ps.setLong(5, book.getCategoryId());
            ps.setString(6, book.getAttribute());
            ps.setLong(7, book.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(Long id) throws Exception {
        String sql = "DELETE FROM books WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<BaseBook> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(createBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<BaseBook> findByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(createBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<BaseBook> findAll() {
        List<BaseBook> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(createBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<BaseBook> search(String keyword) {
        List<BaseBook> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(createBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<BaseBook> findByCategoryId(Long categoryId) {
        List<BaseBook> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE category_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(createBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public void deleteByIsbn(String isbn) throws Exception {
        String sql = "DELETE FROM books WHERE isbn=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("未找到ISBN为 " + isbn + " 的图书");
            }
        }
    }

    private BaseBook createBookFromResultSet(ResultSet rs) throws SQLException {
        BaseBook book;
        Long categoryId = rs.getLong("category_id");
        switch (categoryId.intValue()) {
            case 1 -> book = new ScienceBook();
            case 2 -> book = new RomanceBook();
            case 3 -> book = new MysteryBook();
            case 4 -> book = new HistoryBook();
            case 5 -> book = new ComputerBook();
            case 6 -> book = new MedicineBook();
            case 7 -> book = new LiteratureBook();
            case 8 -> book = new TravelBook();
            case 9 -> book = new BusinessBook();
            case 10 -> book = new ArtsBook();
            default -> throw new SQLException("未知的图书类型ID: " + categoryId);
        }
        
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublicationDate(rs.getDate("publication_date").toLocalDate());
        book.setIsbn(rs.getString("isbn"));
        book.setCategoryId(categoryId);
        book.setAttribute(rs.getString("attribute"));
        book.loadFromAttribute();  // 加载特有属性
        
        return book;
    }
} 