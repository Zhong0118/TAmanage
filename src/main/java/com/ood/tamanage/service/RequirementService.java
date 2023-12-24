package com.ood.tamanage.service;

import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Module;
import com.ood.tamanage.pojo.Requirements;

import java.util.List;

public interface RequirementService {
    int getRequirementsCount();

    void addRequirementByModule(Module lab);

    void addRequirementByCourse(Course lab);

    Requirements getRequirementsByCID(int cid);

    Requirements getRequirementsByMID(int mid);

    void deleteRequirementByCourseId(int cid);

    void deleteRequirementByModuleId(int mid);

    void updateRequirements(Requirements requirements);

    List<Requirements> getRequirementsList();
}
