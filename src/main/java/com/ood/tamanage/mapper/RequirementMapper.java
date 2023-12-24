package com.ood.tamanage.mapper;

import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Module;
import com.ood.tamanage.pojo.Requirements;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RequirementMapper {

    @Select("SELECT COUNT(*) FROM requirements")
    int getRequirementsCount();

    @Insert("INSERT INTO requirements VALUES (null, null, #{mid}, 0, 0, null, 0,false,false)")
    void addRequirementByModule(Module lab);

    @Insert("INSERT INTO requirements VALUES (null, #{cid}, null, 0, 0, null, 0,false,false)")
    void addRequirementByCourse(Course lab);


    @Select("SELECT * FROM requirements WHERE cid = #{cid}")
    Requirements getByCID(int cid);

    @Select("SELECT * FROM requirements WHERE mid = #{mid}")
    Requirements getByMID(int mid);

    @Delete("DELETE FROM requirements WHERE cid = #{cid}")
    void deleteByCourseId(int cid);

    @Delete("DELETE FROM requirements WHERE mid = #{mid}")
    void deleteByModuleId(int mid);

    @Update("UPDATE requirements SET quantity = #{quantity}, workHours = #{workHours}, classGrade = #{classGrade}, " +
            "stageRequirement = #{stageRequirement}, undergrad = #{undergrad}, master = #{master} WHERE rid = #{rid}")
    void updateRequirements(Requirements requirements);

    @Select("SELECT * FROM requirements")
    List<Requirements> getRequirementsList();
}
