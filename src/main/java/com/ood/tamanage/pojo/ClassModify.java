package com.ood.tamanage.pojo;

import com.ood.tamanage.enums.Purpose;
import com.ood.tamanage.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造器
public class ClassModify {
    private int cmid;
    private Purpose purpose;
    private Status status;
    private int cid;
    private int mid;
    private int studentId;
    private int lecturerId;
    private Student student;
}
