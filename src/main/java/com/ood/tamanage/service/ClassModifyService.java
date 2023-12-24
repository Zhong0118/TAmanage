package com.ood.tamanage.service;

import com.ood.tamanage.enums.Purpose;
import com.ood.tamanage.enums.Status;
import com.ood.tamanage.pojo.ClassModify;
import com.ood.tamanage.pojo.Course;

import java.util.List;

public interface ClassModifyService {
    List<ClassModify> getAllByStudent(Integer uid);

    void addCourseModify(Purpose purpose, Status status, int cid, int uid);

    List<ClassModify> getAllByPending();

    List<ClassModify> getAllWithoutPending();

    void updateClassModify(int uid, String courseCode, Status status);

    void deleteClassModifyByCode(String courseCode);

    boolean hasModify(int uid, int cid);
}
