package com.ood.tamanage.pojo;

import com.ood.tamanage.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造器
public class Offers {
    private int oid;
    private int uid;
    private int cid;
    private int mid;
    private Status status;
    private String description;
    private String code;
    private String sid;
    private String username;
}
