package com.ood.tamanage.pojo;

import com.ood.tamanage.enums.UserType;
import lombok.*;

import java.util.List;

@Data  // 生成getter,setter,equals,canEqual,hashCode,toString
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor  // 生成全参构造器
@EqualsAndHashCode(callSuper=false)
public class Lecturer extends User {
    private int uid;
    private String email;
    private List<Course> labCourses;
    private List<Module> labModules;
    private List<Requirements> requirementsList;

}
