package com.ood.tamanage.mapper;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.pojo.Major;
import com.ood.tamanage.pojo.MajorModule;
import com.ood.tamanage.pojo.Module;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ModuleMapper {


    @Select("SELECT * FROM majormodule WHERE mid = #{mid}")
    List<MajorModule> getMajorsForModule(Module module);

    @Select("SELECT * FROM module")
    List<Module> getModulesList();

    @Insert("INSERT INTO module VALUES (null, #{moduleName}, #{moduleCode}, #{stage}, #{trimester}, #{type}, #{day}, #{timeslot}, #{lecturerId})")
    @Options(useGeneratedKeys = true, keyProperty = "mid")
    void addModuleInModule(Module module);

    @Insert("INSERT INTO majormodule VALUES (#{majorCode}, #{module.mid})")
    void addModuleInMajorModule(@Param("module") Module module, @Param("majorCode") String majorCode);

    @Select("SELECT count(*) FROM module WHERE module_code = #{moduleCode}")
    int getModuleCountByModule(Module module);
    @Select("SELECT count(*) FROM module WHERE module_code = #{code}")
    int getModuleCountByCode(String code);


    @Update("<script>" +
            "UPDATE module" +
            "<set>" +
            "<if test='moduleName != null'>module_name = #{moduleName},</if>" +
            "<if test='stage != null'>stage = #{stage},</if>" +
            "<if test='trimester != null'>trimester = #{trimester},</if>" +
            "<if test='day != null'>day = #{day},</if>" +
            "<if test='timeslot != null'>timeslot = #{timeslot},</if>" +
            "<if test='lecturerId != null'>lecturer_id = #{lecturerId}</if>" +
            "</set>" +
            "WHERE module_code = #{moduleCode} and type = #{type}" +
            "</script>")
    void updateModule(Module module);

    @Delete("DELETE FROM majormodule WHERE mid = #{mid}")
    void deleteModuleInMajorModule(int mid);

    @Delete("DELETE FROM module WHERE module_code = #{moduleCode}")
    void deleteModuleInModule(String moduleCode);

    @Select("select * from module where lecturer_id = #{uid} and type = 'Lab'")
    List<Module> getModulesLabListByUid(int uid);

    @Update("update module set lecturer_id = null where lecturer_id = #{uid}")
    void updateModuleLecturer(int uid);

    @Select("SELECT * FROM majormodule WHERE majorCode = #{majorCode}")
    List<MajorModule> getModulesByMajorCode(String majorCode);

    @Select("select count(*) from module where module_code = #{moduleCode} and stage = #{stage} and trimester = #{trimester} and " +
            "((day = #{day} and timeslot = #{timeslot}) or (day = #{day1} and timeslot = #{timeslot1}))")
    int timeConflict(@Param("moduleCode") String moduleCode, @Param("stage") int stage, @Param("day") Day day,
                     @Param("timeslot")Timeslot timeslot, @Param("trimester") Trimester trimester, @Param("day1") Day day1, @Param("timeslot1")Timeslot timeslot1);

    @Select("SELECT * FROM module WHERE mid = #{mid}")
    Module getModuleByMid(int mid);

    @Select("select * from module where module_code = #{moduleCode} and type = 'Lab'")
    Module getModuleByCodeByLab(String moduleCode);

    @Select("select * from module where stage = #{i} and type = 'Lab'")
    List<Module> getLabModulesByStage(int i);

    @Select("select * from module where module_code = #{code} and trimester = #{trimester}")
    List<Module> getModulesByModuleCodeByTrimster(@Param("code") String code, @Param("trimester") Trimester trimester);

    @Select("select * from module where lecturer_id = #{uid} and trimester = #{trimester}")
    List<Module> getModulesListByUidByTrimester(@Param("uid") int uid, @Param("trimester") Trimester trimester);
}
