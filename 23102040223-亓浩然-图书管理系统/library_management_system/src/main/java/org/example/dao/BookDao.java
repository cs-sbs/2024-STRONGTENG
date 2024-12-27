package org.example.dao;

import org.example.entity.BaseBook;
import java.util.List;
import java.util.Optional;

/**
 * 图书数据访问接口
 * 定义对图书数据的基本操作
 */
public interface BookDao {
    /**
     * 插入新图书
     * @param book 图书对象
     * @throws Exception 当插入操作失败时抛出异常
     */
    void insert(BaseBook book) throws Exception;

    /**
     * 更新图书信息
     * @param book 图书对象
     * @throws Exception 当更新操作失败时抛出异常
     */
    void update(BaseBook book) throws Exception;

    /**
     * 根据ISBN删除图书
     * @param isbn 图书ISBN
     * @throws Exception 当删除操作失败时抛出异常
     */
    void deleteByIsbn(String isbn) throws Exception;

    public void deleteById(Long id)throws Exception;
    Optional<BaseBook> findById(Long id);
    Optional<BaseBook> findByIsbn(String isbn);
    List<BaseBook> findAll();
    List<BaseBook> search(String keyword);
    List<BaseBook> findByCategoryId(Long categoryId);
} 