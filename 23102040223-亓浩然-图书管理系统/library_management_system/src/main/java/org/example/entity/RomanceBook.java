package org.example.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class RomanceBook extends BaseBook {
    private String romanceType;    // 爱情类型
    private String settingPeriod;  // 故事背景时期
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void loadFromAttribute() {
        try {
            if (getAttribute() != null) {
                Map<String, String> attrs = mapper.readValue(getAttribute(), Map.class);
                this.romanceType = attrs.get("romanceType");
                this.settingPeriod = attrs.get("settingPeriod");
            }
        } catch (Exception e) {
            throw new RuntimeException("解析爱情类图书属性失败", e);
        }
    }
    
    @Override
    public void saveToAttribute() {
        try {
            Map<String, String> attrs = new HashMap<>();
            attrs.put("romanceType", this.romanceType);
            attrs.put("settingPeriod", this.settingPeriod);
            setAttribute(mapper.writeValueAsString(attrs));
        } catch (Exception e) {
            throw new RuntimeException("保存爱情类图书属性失败", e);
        }
    }
    
    @Override
    public void displayInfo() {
        loadFromAttribute();  // 从attribute加载特有属性
        System.out.println("=== 爱情类图书信息 ===");
        System.out.println("书名：" + getTitle());
        System.out.println("作者：" + getAuthor());
        System.out.println("出版日期：" + getPublicationDate());
        System.out.println("ISBN：" + getIsbn());
        System.out.println("爱情类型：" + romanceType);
        System.out.println("故事背景：" + settingPeriod);
        System.out.println("====================");
    }
} 