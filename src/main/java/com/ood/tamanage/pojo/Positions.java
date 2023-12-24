package com.ood.tamanage.pojo;

import com.ood.tamanage.enums.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造
public class Positions {
    private int pid;
    private int uid;
    private int cid;
    private int mid;
    private Evaluation evaluation;
    private String code;
    private Student student;
    private Course course;
    private Module module;
}
