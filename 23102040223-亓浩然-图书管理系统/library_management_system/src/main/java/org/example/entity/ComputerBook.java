package org.example.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComputerBook extends BaseBook {
    private String programmingLanguage; // 编程语言
    private String technicalField;      // 技术领域
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void loadFromAttribute() {
        try {
            if (getAttribute() != null) {
                Map<String, String> attrs = mapper.readValue(getAttribute(), Map.class);
                this.programmingLanguage = attrs.get("programmingLanguage");
                this.technicalField = attrs.get("technicalField");
            }
        } catch (Exception e) {
            throw new RuntimeException("解析计算机类图书属性失败", e);
        }
    }
    
    @Override
    public void saveToAttribute() {
        try {
            Map<String, String> attrs = new HashMap<>();
            attrs.put("programmingLanguage", this.programmingLanguage);
            attrs.put("technicalField", this.technicalField);
            setAttribute(mapper.writeValueAsString(attrs));
        } catch (Exception e) {
            throw new RuntimeException("保存计算机类图书属性失败", e);
        }
    }
    
    @Override
    public void displayInfo() {
        loadFromAttribute();  // 从attribute加载特有属性
        System.out.println("=== 计算机类图书信息 ===");
        System.out.println("书名：" + getTitle());
        System.out.println("作者：" + getAuthor());
        System.out.println("出版日期：" + getPublicationDate());
        System.out.println("ISBN：" + getIsbn());
        System.out.println("编程语言：" + programmingLanguage);
        System.out.println("技术领域：" + technicalField);
        System.out.println("====================");
    }
} 