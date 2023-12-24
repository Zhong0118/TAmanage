package com.ood.tamanage.service.impl;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.mapper.ModuleMapper;
import com.ood.tamanage.pojo.Major;
import com.ood.tamanage.pojo.MajorModule;
import com.ood.tamanage.pojo.Module;
import com.ood.tamanage.service.ModuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Resource
    private ModuleMapper moduleMapper;
    @Override
    public List<MajorModule> getMajorsForModule(Module module) {
        return moduleMapper.getMajorsForModule(module);
    }

    @Override
    public List<Module> getModulesList() {
        return moduleMapper.getModulesList();
    }

    @Override
    public boolean canAddModule(Module module) {
        int count = moduleMapper.getModuleCountByModule(module);
        return count < 2;
    }

    @Override
    public boolean canAddModule(String code) {
        int count = moduleMapper.getModuleCountByCode(code);
        return count < 2;
    }

    @Override
    public void addModuleInModule(Module module) {
        moduleMapper.addModuleInModule(module);
    }

    @Override
    public void addModuleInMajorModule(Module module, String majorCode) {
        moduleMapper.addModuleInMajorModule(module, majorCode);
    }

    @Override
    public void updateModule(Module lecture) {
        moduleMapper.updateModule(lecture);
    }

    @Override
    public void deleteModuleInMajorModule(int mid) {
        moduleMapper.deleteModuleInMajorModule(mid);
    }

    @Override
    public void deleteModuleInModule(String moduleCode) {
        moduleMapper.deleteModuleInModule(moduleCode);
    }

    @Override
    public List<Module> getModulesLabListByUid(int uid) {
        return moduleMapper.getModulesLabListByUid(uid);
    }

    @Override
    public void updateModuleLecturer(int uid) {
        moduleMapper.updateModuleLecturer(uid);
    }

    @Override
    public boolean timeConflict(Major major, int stage, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1) {
        List<MajorModule> modules = moduleMapper.getModulesByMajorCode(major.getMajorCode());
        List<Integer> moduleIds = new ArrayList<>();
        for (MajorModule module : modules) {
            moduleIds.add(module.getMid());
        }
        for (int mid : moduleIds) {
            String moduleCode = moduleMapper.getModuleByMid(mid).getModuleCode();
            if (moduleMapper.timeConflict(moduleCode, stage, day, timeslot, trimester, day1, timeslot1) >= 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean timeConflictWithoutCode(Major major, int stage, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1, String code) {
        List<MajorModule> modules = moduleMapper.getModulesByMajorCode(major.getMajorCode());
        List<Integer> moduleIds = new ArrayList<>();
        for (MajorModule module : modules) {
            moduleIds.add(module.getMid());
        }
        for (int mid : moduleIds) {
            String moduleCode = moduleMapper.getModuleByMid(mid).getModuleCode();
            if (!Objects.equals(moduleCode, code)){
                if (moduleMapper.timeConflict(moduleCode, stage, day, timeslot, trimester, day1, timeslot1) >= 1) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public Module getModuleByMID(int mid) {
        return moduleMapper.getModuleByMid(mid);
    }

    @Override
    public Module getModuleByCodeByLab(String moduleCode) {
        return moduleMapper.getModuleByCodeByLab(moduleCode);
    }

    @Override
    public List<Module> getLabModulesByStage(int i) {
        return moduleMapper.getLabModulesByStage(i);
    }

    @Override
    public List<Module> getModulesByMajorByStageByTrimester(String majorCode, Integer stage, Trimester trimester) {
        List<Module> labModuleList = moduleMapper.getLabModulesByStage(stage);
        List<String> moduleCodeList = new ArrayList<>();
        List<Module> result = new ArrayList<>();
        for (Module module: labModuleList) {
            List<MajorModule> majorModuleList = moduleMapper.getMajorsForModule(module);
            List<String> majorCodeList = new ArrayList<>();
            for (MajorModule majorModule: majorModuleList) {
                majorCodeList.add(majorModule.getMajorCode());
            }
            if (majorCodeList.contains(majorCode)){
                moduleCodeList.add(module.getModuleCode());
            }
        }
        for (String code:moduleCodeList) {
            List<Module> modules = moduleMapper.getModulesByModuleCodeByTrimster(code,trimester);
            result.addAll(modules);
        }
        return result;
    }

    @Override
    public List<Module> getModulesListByUidByTrimester(int uid, Trimester trimester) {
        return moduleMapper.getModulesListByUidByTrimester(uid, trimester);
    }

}
