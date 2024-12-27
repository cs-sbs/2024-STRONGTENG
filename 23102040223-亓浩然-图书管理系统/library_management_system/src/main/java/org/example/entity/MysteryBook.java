package org.example.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class MysteryBook extends BaseBook {
    private String subgenre;      // 子类型（推理/悬疑/犯罪等）
    private String detectiveType; // 侦探类型
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void loadFromAttribute() {
        try {
            if (getAttribute() != null) {
                Map<String, String> attrs = mapper.readValue(getAttribute(), Map.class);
                this.subgenre = attrs.get("subgenre");
                this.detectiveType = attrs.get("detectiveType");
            }
        } catch (Exception e) {
            throw new RuntimeException("解析悬疑类图书属性失败", e);
        }
    }
    
    @Override
    public void saveToAttribute() {
        try {
            Map<String, String> attrs = new HashMap<>();
            attrs.put("subgenre", this.subgenre);
            attrs.put("detectiveType", this.detectiveType);
            setAttribute(mapper.writeValueAsString(attrs));
        } catch (Exception e) {
            throw new RuntimeException("保存悬疑类图书属性失败", e);
        }
    }
    
    @Override
    public void displayInfo() {
        loadFromAttribute();  // 从attribute加载特有属性
        System.out.println("=== 悬疑类图书信息 ===");
        System.out.println("书名：" + getTitle());
        System.out.println("作者：" + getAuthor());
        System.out.println("出版日期：" + getPublicationDate());
        System.out.println("ISBN：" + getIsbn());
        System.out.println("子类型：" + subgenre);
        System.out.println("侦探类型：" + detectiveType);
        System.out.println("====================");
    }
} 