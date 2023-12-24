package com.ood.tamanage.pojo;

import com.ood.tamanage.enums.UserType;
import lombok.*;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造器
@EqualsAndHashCode(callSuper=false)
public class Admin extends User{
    private int uid;
    private String email;

}
