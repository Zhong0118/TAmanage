package com.ood.tamanage.pojo;

import com.ood.tamanage.enums.EducationStatus;
import lombok.*;

import java.util.List;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造器
@EqualsAndHashCode(callSuper=false)
public class Student extends User{
    private Integer uid;
    private String email;
    private String studentId;
    private EducationStatus educationStatus;
    private Integer stage;
    private String majorCode;
    private Integer timeLimitation;


    private List<Course> labCourses;
    private List<Course> lectureCourses;
    private List<Module> labModules;
    private List<Module> lectureModules;

}
