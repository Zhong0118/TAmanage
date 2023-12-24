package com.ood.tamanage.service.impl;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.mapper.CourseMapper;
import com.ood.tamanage.mapper.LecturerMapper;
import com.ood.tamanage.pojo.Lecturer;
import com.ood.tamanage.pojo.Result;
import com.ood.tamanage.pojo.User;
import com.ood.tamanage.service.LecturerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LecturerServiceImpl implements LecturerService {

    @Resource
    private LecturerMapper lecturerMapper;

    @Override
    public List<Lecturer> getLecturersList() {
        return lecturerMapper.getLecturersList();
    }

    @Override
    public Lecturer getLecturerById(int uid) {
        return lecturerMapper.getLecturerById(uid);
    }

    @Override
    public void addLecturer(Lecturer lecturer) {
        lecturerMapper.addLecturer(lecturer);

    }

    @Override
    public void updateLecturer(Lecturer lecturer) {
        lecturerMapper.updateLecturer(lecturer);
    }

    @Override
    public void deleteLecturer(int uid) {
        lecturerMapper.deleteLecturer(uid);
    }

    @Override
    public boolean timeConflict(Lecturer lecturer, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1) {
        int count1 = lecturerMapper.timeConflict1(lecturer, day, timeslot, trimester, day1, timeslot1);
        int count2 = lecturerMapper.timeConflict2(lecturer, day1, timeslot1, trimester, day, timeslot);
        return count1 + count2 >= 1;
    }

    @Override
    public boolean timeConflictWithoutCode(Lecturer lecturer, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1, String courseCode) {
        int count1 = lecturerMapper.timeConflictWithoutCode1(lecturer, day, timeslot, trimester, day1, timeslot1, courseCode);
        int count2 = lecturerMapper.timeConflictWithoutCode2(lecturer, day1, timeslot1, trimester, day, timeslot, courseCode);
        return count1 + count2 >= 1;
    }

    @Override
    public User validate(User user) {
        return lecturerMapper.validate(user);

    }
}
