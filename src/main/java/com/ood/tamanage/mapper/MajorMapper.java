package com.ood.tamanage.mapper;

import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Major;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MajorMapper {
    @Select("select * from Major")
    List<Major> getMajorsList();

    @Select("select * from Major where mid = #{mid}")
    Major getMajorById(int mid);

    @Select("select * from Major where majorCode = #{majorCode}")
    Major getMajorByCode(String majorCode);

    @Insert("insert into Major values (null, #{majorName}, #{majorCode})")
    @Options(useGeneratedKeys = true, keyProperty = "mid")
    void addMajor(Major major);


    @Update("<script>" +
            "UPDATE major" +
            "<set>" +
            "<if test='majorCode != null'>majorCode = #{majorCode},</if>" +
            "<if test='majorName != null'>major_name = #{majorName}</if>" +
            "</set>" +
            "WHERE mid = #{mid}" +
            "</script>")
    void updateMajor(Major major);

    @Delete("delete from Major where mid = #{mid}")
    void deleteMajor(int mid);

    @Select("select * from Course where major_code = #{majorCode}")
    List<Course> getCourses(Major major);

    @Select("select * from Course where majorCode = #{major.majorCode} and stage = #{stage}")
    List<Course> getCoursesByStage(@Param("major") Major major, @Param("stage") int stage);


    @Insert("insert into Course values " +
            "(null, #{course.courseName}, #{course.courseCode}, #{major.majorCode}, " +
            "#{course.stage}, #{course.trimester}, #{course.type}, #{course.day}, #{course.timeslot}, #{course.lecturerId})")
    @Options(useGeneratedKeys = true, keyProperty = "course.cid")
    void addCourse(@Param("major") Major major, @Param("course") Course course);

    @Select("select count(*) / 2 from Course where majorCode = #{majorCode}")
    int getCoursesCount(Major major);
    @Select("select count(*)  from MajorModule where majorCode = #{majorCode}")
    int getModulesCount(Major major);
    @Select("select count(*) / 2 from Course where majorCode = #{major.majorCode} and stage = #{stage}")
    int getCoursesCountByStage(@Param("major") Major major, @Param("stage") int stage);
}
