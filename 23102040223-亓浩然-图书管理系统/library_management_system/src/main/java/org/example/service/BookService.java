package org.example.service;

import org.example.entity.BaseBook;
import java.util.List;

/**
 * 图书服务接口
 * 提供图书的增删改查功能
 */
public interface BookService {
    /**
     * 添加新图书
     * @param book 图书对象
     * @throws Exception 当图书信息无效或操作失败时抛出异常
     */
    void addBook(BaseBook book) throws Exception;

    /**
     * 根据ISBN删除图书
     * @param isbn 图书的ISBN
     * @throws Exception 当ISBN不存在或删除失败时抛出异常
     */
    void deleteByIsbn(String isbn) throws Exception;

    /**
     * 获取所有图书
     * @return 图书列表
     */
    List<BaseBook> getAllBooks();

    /**
     * 搜索图书
     * @param keyword 搜索关键词（可匹配书名、作者、ISBN）
     * @return 匹配的图书列表
     */
    List<BaseBook> searchBooks(String keyword);

    /**
     * 根据分类ID获取图书
     * @param categoryId 分类ID
     * @return 该分类下的图书列表
     */
    List<BaseBook> getBooksByCategory(Long categoryId);
} 