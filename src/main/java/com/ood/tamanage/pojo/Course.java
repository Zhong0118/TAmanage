package com.ood.tamanage.pojo;

import com.ood.tamanage.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造器
public class Course {
    private int cid;
    private String courseName;
    private String courseCode;
    private String majorCode;
    private int stage;
    private Trimester trimester;
    private Type type;
    private Day day;
    private Timeslot timeslot;
    private int lecturerId;
    private Lecturer lecturer;
    private Purpose purpose;
    private Status status;
    private Student student;

}
