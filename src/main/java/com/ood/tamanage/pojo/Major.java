package com.ood.tamanage.pojo;

import lombok.*;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造器
public class Major {
    private int mid;
    private String majorName;
    private String majorCode;
}
