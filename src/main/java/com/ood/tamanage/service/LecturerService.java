package com.ood.tamanage.service;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.pojo.Admin;
import com.ood.tamanage.pojo.Lecturer;

import java.util.List;

public interface LecturerService extends UserService {
    List<Lecturer> getLecturersList();

    Lecturer getLecturerById(int uid);

    void addLecturer(Lecturer lecturer);

    void updateLecturer(Lecturer lecturer);

    void deleteLecturer(int uid);

    boolean timeConflict(Lecturer lecturer, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1);

    boolean timeConflictWithoutCode(Lecturer lecturer, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1, String courseCode);
}
