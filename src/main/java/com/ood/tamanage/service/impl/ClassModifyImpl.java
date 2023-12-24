package com.ood.tamanage.service.impl;

import com.ood.tamanage.enums.Purpose;
import com.ood.tamanage.enums.Status;
import com.ood.tamanage.mapper.ClassModifyMapper;
import com.ood.tamanage.mapper.CourseMapper;
import com.ood.tamanage.pojo.ClassModify;
import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.service.ClassModifyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassModifyImpl implements ClassModifyService {
    @Resource
    private ClassModifyMapper classModifyMapper;
    @Resource
    private CourseMapper courseMapper;
    @Override
    public List<ClassModify> getAllByStudent(Integer uid) {
        return classModifyMapper.getAllByStudent(uid);
    }

    @Override
    public void addCourseModify(Purpose purpose, Status status, int cid, int uid) {
        classModifyMapper.addCourseModify(purpose, status, cid, uid);
    }

    @Override
    public List<ClassModify> getAllByPending() {
        return classModifyMapper.getAllByPending();
    }

    @Override
    public List<ClassModify> getAllWithoutPending() {
        return classModifyMapper.getAllWithoutPending();
    }

    @Override
    public void updateClassModify(int uid, String courseCode, Status status) {
        List<Course> courses = courseMapper.getCourseByCode(courseCode);
        for (Course course : courses) {
            System.out.println(course.getCid());
            classModifyMapper.updateCourseModify(uid, course.getCid(), status);
        }
    }

    @Override
    public void deleteClassModifyByCode(String courseCode) {
        List<Course> courses = courseMapper.getCourseByCode(courseCode);
        for (Course course : courses) {
            classModifyMapper.deleteCourseModifyByCid(course.getCid());
        }
    }

    @Override
    public boolean hasModify(int uid, int cid) {
        int count = classModifyMapper.hasModify(uid, cid);
        return count > 0;
    }
}
