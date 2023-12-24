package com.ood.tamanage.service;

import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Major;

import java.util.List;

public interface MajorService {
    List<Major> getMajorsList();
    Major getMajorById(int mid);
    Major getMajorByCode(String majorCode);

    void addMajor(Major major);
    void updateMajor(Major major);
    void deleteMajor(int mid);

    List<Course> getCourses(Major major);

    List<Course> getCoursesByStage(Major major, int stage);

    void addCourse(Major major, Course course);

    int getClassesCount(Major major);
    int getCoursesCountByStage(Major major, int stage);

}
