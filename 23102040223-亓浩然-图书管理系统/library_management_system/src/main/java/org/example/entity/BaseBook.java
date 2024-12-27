package org.example.entity;

import lombok.Data;
import java.time.LocalDate;

/**
 * 图书基类，所有具体类型的图书都继承自此类
 * 包含图书的基本属性和抽象方法
 */
@Data
public abstract class BaseBook {
    private Long id;                // 图书ID，数据库主键
    private String title;           // 图书标题
    private String author;          // 作者
    private LocalDate publicationDate; // 出版日期
    private String isbn;            // ISBN号
    private Long categoryId;        // 分类ID
    private String attribute;       // JSON格式存储的特有属性
    
    /**
     * 显示图书信息的抽象方法
     * 每种类型的图书都需要实现自己的显示逻辑
     */
    public abstract void displayInfo();

    /**
     * 从attribute字段加载特有属性
     * 将JSON格式的attribute解析为具体的属性值
     */
    public abstract void loadFromAttribute();

    /**
     * 将特有属性保存到attribute字段
     * 将具体的属性值序列化为JSON格式
     */
    public abstract void saveToAttribute();
} 