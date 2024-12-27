package org.example.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class HistoryBook extends BaseBook {
    private String historicalPeriod;    // 历史时期
    private String geographicalRegion;  // 地理区域
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void loadFromAttribute() {
        try {
            if (getAttribute() != null) {
                Map<String, String> attrs = mapper.readValue(getAttribute(), Map.class);
                this.historicalPeriod = attrs.get("historicalPeriod");
                this.geographicalRegion = attrs.get("geographicalRegion");
            }
        } catch (Exception e) {
            throw new RuntimeException("解析历史类图书属性失败", e);
        }
    }
    
    @Override
    public void saveToAttribute() {
        try {
            Map<String, String> attrs = new HashMap<>();
            attrs.put("historicalPeriod", this.historicalPeriod);
            attrs.put("geographicalRegion", this.geographicalRegion);
            setAttribute(mapper.writeValueAsString(attrs));
        } catch (Exception e) {
            throw new RuntimeException("保存历史类图书属性失败", e);
        }
    }
    
    @Override
    public void displayInfo() {
        loadFromAttribute();  // 从attribute加载特有属性
        System.out.println("=== 历史类图书信息 ===");
        System.out.println("书名：" + getTitle());
        System.out.println("作者：" + getAuthor());
        System.out.println("出版日期：" + getPublicationDate());
        System.out.println("ISBN：" + getIsbn());
        System.out.println("历史时期：" + historicalPeriod);
        System.out.println("地理区域：" + geographicalRegion);
        System.out.println("====================");
    }
} 