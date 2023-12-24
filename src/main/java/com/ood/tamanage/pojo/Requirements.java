package com.ood.tamanage.pojo;

import com.ood.tamanage.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造器
public class Requirements {
    private int rid;
    private int cid;
    private int mid;
    private int quantity;
    private int workHours;
    private Grade classGrade;
    private int stageRequirement;
    private boolean undergrad;
    private boolean master;
    private String code;
}
