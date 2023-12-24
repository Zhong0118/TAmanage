package com.ood.tamanage.service;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Major;

import java.util.List;

public interface CourseService {
    boolean canAddCourse(Course course);
    boolean canAddCourse(String courseCode);


    void deleteCourse(String courseCode);

    void updateCourse(String courseCode, Course course);

    List<Course> getCoursesLabListByUid(int uid);
    void updateCourseLecturer(int uid);

    boolean timeConflict(Major major, int stage, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1);

    boolean timeConflictWithoutCode(Major major, int stage, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1, String courseCode);

    Course getCourseByCID(int cid);

    Course getCourseByCodeByLab(String courseCode);

    List<Course> getLabCoursesByMajorByStage(String majorCode, int i);

    List<Course> getCourseByMajorByStageByTrimester(String majorCode, int stage, Trimester trimester);

    List<Course> getCoursesListByUidByTrimester(int uid, Trimester trimester);

    List<Course> getCourseByCode(String courseCode);
}
