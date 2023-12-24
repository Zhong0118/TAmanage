package com.ood.tamanage.service;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.pojo.Major;
import com.ood.tamanage.pojo.MajorModule;
import com.ood.tamanage.pojo.Module;

import java.util.List;

public interface ModuleService {
    List<MajorModule> getMajorsForModule(Module module);
    List<Module> getModulesList();

    boolean canAddModule(Module module);
    boolean canAddModule(String code);
    void addModuleInModule(Module module);
    void addModuleInMajorModule(Module module, String majorCode);

    void updateModule(Module lecture);

    void deleteModuleInMajorModule(int mid);

    void deleteModuleInModule(String moduleCode);

    List<Module> getModulesLabListByUid(int uid);

    void updateModuleLecturer(int uid);

    boolean timeConflict(Major major, int stage, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1);

    boolean timeConflictWithoutCode(Major major, int stage, Day day, Timeslot timeslot, Trimester trimester, Day day1, Timeslot timeslot1, String moduleCode);

    Module getModuleByMID(int mid);

    Module getModuleByCodeByLab(String moduleCode);

    List<Module> getLabModulesByStage(int i);

    List<Module> getModulesByMajorByStageByTrimester(String majorCode, Integer stage, Trimester trimester);

    List<Module> getModulesListByUidByTrimester(int uid, Trimester trimester);
}
