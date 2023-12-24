package com.ood.tamanage.service.impl;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.mapper.AdminMapper;
import com.ood.tamanage.mapper.CourseMapper;
import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Major;
import com.ood.tamanage.service.CourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public boolean canAddCourse(Course course) {
        int count = courseMapper.getCourseCountByCourse(course);
        return count < 2;
    }

    @Override
    public boolean canAddCourse(String courseCode) {
        int count = courseMapper.getCourseCountByCode(courseCode);
        return count < 2;
    }


    @Override
    public void deleteCourse(String courseCode) {
        courseMapper.deleteCourse(courseCode);
    }

    @Override
    public void updateCourse(String courseCode, Course course) {
        courseMapper.updateCourse(courseCode, course);
    }

    @Override
    public List<Course> getCoursesLabListByUid(int uid) {
        return courseMapper.getCoursesLabListByUid(uid);
    }

    @Override
    public void updateCourseLecturer(int uid) {
        courseMapper.updateCourseLecturer(uid);
    }

    @Override
    public boolean timeConflict(Major major, int stage, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1) {
        int count = courseMapper.timeConflict(major, stage, day, timeslot, trimester, day1, timeslot1);
        return count >= 1;
    }

    @Override
    public boolean timeConflictWithoutCode(Major major, int stage, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1, String courseCode) {
        int count = courseMapper.timeConflictWithoutCode(major, stage, day, timeslot, trimester, day1, timeslot1, courseCode);
        return count>=1;
    }

    @Override
    public Course getCourseByCID(int cid) {
        return courseMapper.getCourseByCID(cid);
    }

    @Override
    public Course getCourseByCodeByLab(String courseCode) {
        return courseMapper.getCourseByCodeByLab(courseCode);
    }

    @Override
    public List<Course> getLabCoursesByMajorByStage(String majorCode, int i) {
        return courseMapper.getLabCoursesByMajorByStage(majorCode, i);
    }

    @Override
    public List<Course> getCourseByMajorByStageByTrimester(String majorCode, int stage, Trimester trimester) {
        return courseMapper.getCourseByMajorByStageByTrimester(majorCode, stage, trimester);
    }

    @Override
    public List<Course> getCoursesListByUidByTrimester(int uid, Trimester trimester) {
        return courseMapper.getCoursesListByUidByTrimester(uid, trimester);
    }

    @Override
    public List<Course> getCourseByCode(String courseCode) {
        return courseMapper.getCourseByCode(courseCode);
    }
}
