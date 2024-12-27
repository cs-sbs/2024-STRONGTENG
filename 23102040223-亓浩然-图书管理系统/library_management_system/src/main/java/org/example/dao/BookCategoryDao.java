package org.example.dao;

import org.example.entity.BookCategory;
import java.util.List;
import java.util.Optional;

public interface BookCategoryDao {
    void insert(BookCategory category);
    void update(BookCategory category);
    void deleteById(Long id);
    Optional<BookCategory> findById(Long id);
    Optional<BookCategory> findByName(String name);
    List<BookCategory> findAll();
} 