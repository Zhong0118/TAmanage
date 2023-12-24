package com.ood.tamanage.service.impl;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.mapper.ApplicationMapper;
import com.ood.tamanage.mapper.CourseMapper;
import com.ood.tamanage.mapper.StudentMapper;
import com.ood.tamanage.pojo.*;
import com.ood.tamanage.service.ApplicationService;
import com.ood.tamanage.service.ClassModifyService;
import com.ood.tamanage.service.CourseService;
import com.ood.tamanage.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private ApplicationMapper applicationMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CourseService courseService;
    @Resource
    private ClassModifyService classModifyService;

    @Override
    public List<Student> getStudentsList() {
        return studentMapper.getStudentsList();
    }

    @Override
    public Student getStudentById(int uid) {
        return studentMapper.getStudentById(uid);
    }

    @Override
    public Student getStudentByStudentId(String studentId) {
        return studentMapper.getStudentByStudentId(studentId);
    }

    @Override
    public void addStudent(Student student) {
        studentMapper.addStudent(student);
    }

    @Override
    public void updateStudent(Student student) {
        studentMapper.updateStudent(student);
    }

    @Override
    public void deleteStudent(int uid) {
        studentMapper.deleteStudent(uid);
    }

    @Override
    public boolean applyForCourse(int uid, int classId) {
        return studentMapper.applyForCourse(uid, classId) > 0;
    }
    @Override
    public boolean applyForModule(int uid, int classId) {
        return studentMapper.applyForModule(uid, classId) != 0;
    }

    @Override
    public boolean checkTimeConflict(int uid, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1) {
        Student student = studentMapper.getStudentById(uid);  // 获取学生信息
        // 得到这个学生原始的课
        List<Course> courses = courseService.getCourseByMajorByStageByTrimester(student.getMajorCode(), student.getStage(), trimester);
        // 得到这个学生的所有课程修改申请
        List<ClassModify> courseModify = classModifyService.getAllByStudent(student.getUid());
        List<Course> appendCourse = new ArrayList<>();
        List<Course> removeCourse = new ArrayList<>();
        for (ClassModify classModify : courseModify) {
            if (classModify.getPurpose().name().equals("Append")) {
                Course course = courseService.getCourseByCID(classModify.getCid());
                course.setPurpose(classModify.getPurpose());
                course.setStatus(classModify.getStatus());
                appendCourse.add(course);
            } else {
                Course course = courseService.getCourseByCID(classModify.getCid());
                course.setPurpose(classModify.getPurpose());
                course.setStatus(classModify.getStatus());
                removeCourse.add(course);
            }
        }
        for (Course course : appendCourse) {
            if (course.getStatus().name().equals("Accepted")) {
                courses.add(course);
            }
        }
        for (Course course : removeCourse) {
            if (course.getStatus().name().equals("Accepted")) {
                for (Course c:courses) {
                    if (c.getCid() == course.getCid()) {
                        courses.remove(c);
                        break;
                    }
                }
            }
        }
        // 此时的course就是最终course
        for (Course course : courses) {
            if (course.getDay().name().equals(day.name()) && course.getTimeslot().name().equals(timeslot.name())) {
                return true;
            }
            if (course.getDay().name().equals(day1.name()) && course.getTimeslot().name().equals(timeslot1.name())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean workHoursExceed(int uid, int timeLimitation, int workHours) {
        List<Positions> positionsList = getPositionsListByUid(uid);
        int sum = 0;
        for (Positions positions : positionsList) {
            List<Requirements> requirements = applicationMapper.getRequirementsByCMId(positions.getCid(), positions.getMid());
            for (Requirements requirement : requirements){
                sum += requirement.getWorkHours();
            }
        }
        sum += workHours;
        return sum > timeLimitation;
    }

    @Override
    public List<Positions> getPositionsListByUid(int uid) {
        return studentMapper.getPositionsListByUid(uid);
    }



    @Override
    public User validate(User user) {
        return studentMapper.validate(user);
    }
}
