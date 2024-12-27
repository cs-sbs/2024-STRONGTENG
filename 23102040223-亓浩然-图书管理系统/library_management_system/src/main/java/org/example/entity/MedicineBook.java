package org.example.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicineBook extends BaseBook {
    private String medicalSpecialty;    // 医学专业（内科/外科/儿科等）
    private String targetAudience;      // 目标读者（医生/学生/研究者）
    private String researchContent;     // 研究内容
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void loadFromAttribute() {
        try {
            if (getAttribute() != null) {
                Map<String, String> attrs = mapper.readValue(getAttribute(), Map.class);
                this.medicalSpecialty = attrs.get("medicalSpecialty");
                this.targetAudience = attrs.get("targetAudience");
                this.researchContent = attrs.get("researchContent");
            }
        } catch (Exception e) {
            throw new RuntimeException("解析医学类图书属性失败", e);
        }
    }
    
    @Override
    public void saveToAttribute() {
        try {
            Map<String, String> attrs = new HashMap<>();
            attrs.put("medicalSpecialty", this.medicalSpecialty);
            attrs.put("targetAudience", this.targetAudience);
            attrs.put("researchContent", this.researchContent);
            setAttribute(mapper.writeValueAsString(attrs));
        } catch (Exception e) {
            throw new RuntimeException("保存医学类图书属性失败", e);
        }
    }
    
    @Override
    public void displayInfo() {
        loadFromAttribute();  // 从attribute加载特有属性
        System.out.println("=== 医学类图书信息 ===");
        System.out.println("书名：" + getTitle());
        System.out.println("作者：" + getAuthor());
        System.out.println("出版日期：" + getPublicationDate());
        System.out.println("ISBN：" + getIsbn());
        System.out.println("医学专业：" + medicalSpecialty);
        System.out.println("目标读者：" + targetAudience);
        System.out.println("研究内容：" + researchContent);
        System.out.println("====================");
    }

}