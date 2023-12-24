package com.ood.tamanage.service.impl;


import com.ood.tamanage.mapper.RequirementMapper;
import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Module;
import com.ood.tamanage.pojo.Requirements;
import com.ood.tamanage.service.RequirementService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementServiceImpl implements RequirementService {

    @Resource
    private RequirementMapper requirementMapper;

    @Override
    public int getRequirementsCount() {
        return requirementMapper.getRequirementsCount();
    }

    @Override
    public void addRequirementByModule(Module lab) {
        requirementMapper.addRequirementByModule(lab);
    }

    @Override
    public void addRequirementByCourse(Course lab) {
        requirementMapper.addRequirementByCourse(lab);

    }

    @Override
    public Requirements getRequirementsByCID(int cid) {
        return requirementMapper.getByCID(cid);
    }

    @Override
    public Requirements getRequirementsByMID(int mid) {
        return requirementMapper.getByMID(mid);
    }

    @Override
    public void deleteRequirementByCourseId(int cid) {
        requirementMapper.deleteByCourseId(cid);
    }

    @Override
    public void deleteRequirementByModuleId(int mid) {
        requirementMapper.deleteByModuleId(mid);
    }

    @Override
    public void updateRequirements(Requirements requirements) {
        requirementMapper.updateRequirements(requirements);
    }

    @Override
    public List<Requirements> getRequirementsList() {
        return requirementMapper.getRequirementsList();
    }
}
