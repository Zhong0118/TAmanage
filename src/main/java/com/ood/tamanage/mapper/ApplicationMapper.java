package com.ood.tamanage.mapper;

import com.ood.tamanage.enums.Grade;
import com.ood.tamanage.enums.Status;
import com.ood.tamanage.pojo.Applications;
import com.ood.tamanage.pojo.Requirements;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ApplicationMapper {

    @Select("SELECT COUNT(*) FROM applications")
    int getApplicationsCount();

    @Select("SELECT * FROM requirements WHERE cid = #{cid} and mid = #{mid}")
    List<Requirements> getRequirementsByCMId(@Param("cid") int cid, @Param("mid") int mid);

    @Insert("INSERT INTO applications VALUES (null, #{uid}, #{cid}, null, #{status}, #{studentGrade}, #{studentProject}, #{description})")
    void addApplicationByCid(@Param("uid") int uid, @Param("cid")int classId, @Param("status")Status status, @Param("studentGrade")Grade studentGrade,
                             @Param("studentProject")Grade studentProject, @Param("description")String description);

    @Insert("INSERT INTO applications VALUES (null, #{uid}, null, #{mid}, #{status}, #{studentGrade}, #{studentProject}, #{description})")
    void addApplicationByMid(@Param("uid") int uid, @Param("mid")int classId, @Param("status")Status status, @Param("studentGrade")Grade studentGrade,
                             @Param("studentProject")Grade studentProject, @Param("description")String description);

    @Select("SELECT * FROM applications WHERE status = #{status}")
    List<Applications> getApplicationsListByStatus(Status status);

    @Update("UPDATE applications SET status = #{applicationStatus} WHERE aid = #{aid}")
    void updateApplication(@Param("aid") int aid, @Param("applicationStatus") Status applicationStatus);

    @Select("SELECT * FROM applications WHERE uid = #{uid}")
    List<Applications> getApplicationsListByUid(Integer uid);

    @Select("SELECT * FROM applications WHERE cid = #{cid}")
    List<Applications> getApplicationsByCID(int cid);

    @Select("SELECT * FROM applications WHERE mid = #{mid}")
    List<Applications> getApplicationsByMID(int mid);

    @Select("SELECT * FROM applications WHERE aid = #{aid}")
    Applications getApplicationById(int aid);

    @Delete("DELETE FROM applications WHERE cid = #{cid}")
    void deleteApplicationByCourseId(int cid);

    @Delete("DELETE FROM applications WHERE mid = #{mid}")
    void deleteApplicationByModuleId(int mid);
}
