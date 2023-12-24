package com.ood.tamanage.service;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.pojo.Positions;
import com.ood.tamanage.pojo.Student;

import java.util.List;

public interface StudentService extends UserService {
    List<Student> getStudentsList();

    Student getStudentById(int uid);

    Student getStudentByStudentId(String studentId);

    void addStudent(Student student);

    void updateStudent(Student student);

    void deleteStudent(int uid);

    boolean applyForCourse(int uid, int classId);

    boolean workHoursExceed(int uid, int timeLimitation, int workHours);
    List<Positions> getPositionsListByUid(int uid);

    boolean applyForModule(int uid, int classId);

    boolean checkTimeConflict(int uid, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1);
}
