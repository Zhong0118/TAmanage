package com.ood.tamanage.service;

import com.ood.tamanage.enums.Grade;
import com.ood.tamanage.enums.Status;
import com.ood.tamanage.mapper.ApplicationMapper;
import com.ood.tamanage.pojo.Applications;

import java.util.List;

public interface ApplicationService {
    int getApplicationsCount();

    void addApplication(int uid, int classId, Status status, Grade studentGrade, Grade studentProject, String description);

    void addApplicationByM(int uid, int classId, Status status, Grade studentGrade, Grade studentProject, String description);

    List<Applications> getApplicationsListByStatus(Status status);

    void updateApplication(int aid, Status applicationStatus);

    List<Applications> getApplicationsListByUid(Integer uid);

    List<Applications> getApplicationsByCID(int cid);

    List<Applications> getApplicationsByMID(int mid);

    Applications getApplicationById(int aid);

    void deleteApplicationByCourseId(int cid);

    void deleteApplicationByModuleId(int mid);
}
