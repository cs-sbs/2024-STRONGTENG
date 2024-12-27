package org.example.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class LiteratureBook extends BaseBook {
    private String genre;
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void loadFromAttribute() {
        try {
            if (getAttribute() != null) {
                Map<String, String> attrs = mapper.readValue(getAttribute(), Map.class);
                this.genre = attrs.get("genre");
            }
        } catch (Exception e) {
            throw new RuntimeException("解析文学类图书属性失败", e);
        }
    }
    
    @Override
    public void saveToAttribute() {
        try {
            Map<String, String> attrs = new HashMap<>();
            attrs.put("genre", this.genre);
            setAttribute(mapper.writeValueAsString(attrs));
        } catch (Exception e) {
            throw new RuntimeException("保存文学类图书属性失败", e);
        }
    }
    
    @Override
    public void displayInfo() {
        loadFromAttribute();
        System.out.println("=== 文学类图书信息 ===");
        System.out.println("书名：" + getTitle());
        System.out.println("作者：" + getAuthor());
        System.out.println("出版日期：" + getPublicationDate());
        System.out.println("ISBN：" + getIsbn());
        System.out.println("文学流派：" + genre);
        System.out.println("====================");
    }
} 