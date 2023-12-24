package com.ood.tamanage.service.impl;

import com.ood.tamanage.enums.Grade;
import com.ood.tamanage.enums.Status;
import com.ood.tamanage.mapper.AdminMapper;
import com.ood.tamanage.mapper.ApplicationMapper;
import com.ood.tamanage.pojo.Applications;
import com.ood.tamanage.service.ApplicationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Resource
    private ApplicationMapper applicationMapper;
    @Override
    public int getApplicationsCount() {
        return applicationMapper.getApplicationsCount();
    }

    @Override
    public void addApplication(int uid, int classId, Status status, Grade studentGrade, Grade studentProject, String description) {
        applicationMapper.addApplicationByCid(uid, classId, status, studentGrade, studentProject, description);
    }

    @Override
    public void addApplicationByM(int uid, int classId, Status status, Grade studentGrade, Grade studentProject, String description) {
        applicationMapper.addApplicationByMid(uid, classId, status, studentGrade, studentProject, description);
    }

    @Override
    public List<Applications> getApplicationsListByStatus(Status status) {
        return applicationMapper.getApplicationsListByStatus(status);
    }

    @Override
    public void updateApplication(int aid, Status applicationStatus) {
        applicationMapper.updateApplication(aid, applicationStatus);
    }

    @Override
    public List<Applications> getApplicationsListByUid(Integer uid) {
        return applicationMapper.getApplicationsListByUid(uid);
    }

    @Override
    public List<Applications> getApplicationsByCID(int cid) {
        return applicationMapper.getApplicationsByCID(cid);
    }

    @Override
    public List<Applications> getApplicationsByMID(int mid) {
        return applicationMapper.getApplicationsByMID(mid);
    }

    @Override
    public Applications getApplicationById(int aid) {
        return applicationMapper.getApplicationById(aid);
    }

    @Override
    public void deleteApplicationByCourseId(int cid) {
        applicationMapper.deleteApplicationByCourseId(cid);
    }

    @Override
    public void deleteApplicationByModuleId(int mid) {
        applicationMapper.deleteApplicationByModuleId(mid);
    }

}
