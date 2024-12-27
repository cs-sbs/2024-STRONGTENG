package org.example.service.impl;

import org.example.dao.BookDao;
import org.example.dao.impl.BookDaoImpl;
import org.example.entity.BaseBook;
import org.example.service.BookService;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private final BookDao bookDao = new BookDaoImpl();

    @Override
    public void addBook(BaseBook book) throws Exception {
        if (book == null) {
            throw new IllegalArgumentException("图书信息不能为空");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("图书标题不能为空");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN不能为空");
        }

        bookDao.insert(book);
    }

    @Override
    public void deleteByIsbn(String isbn) throws Exception {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN不能为空");
        }
        
        // 先检查图书是否存在
        Optional<BaseBook> book = bookDao.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new Exception("未找到ISBN为 " + isbn + " 的图书");
        }
        
        bookDao.deleteByIsbn(isbn);
    }

    @Override
    public List<BaseBook> getAllBooks() {
        return bookDao.findAll();
    }

    @Override
    public List<BaseBook> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDao.search(keyword);
    }

    @Override
    public List<BaseBook> getBooksByCategory(Long categoryId) {
        return bookDao.findByCategoryId(categoryId);
    }
} 