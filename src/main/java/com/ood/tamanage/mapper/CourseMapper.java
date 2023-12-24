package com.ood.tamanage.mapper;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Major;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CourseMapper {

    @Select("select count(*) from course where course_code = #{courseCode}")
    int getCourseCountByCourse(Course course);
    @Select("select count(*) from course where course_code = #{courseCode}")
    int getCourseCountByCode(String courseCode);


    @Delete("delete from course where course_code = #{courseCode}")
    void deleteCourse(String courseCode);

    @Update("<script>" +
            "UPDATE course" +
            "<set>" +
            "<if test='course.courseName != null'>course_name = #{course.courseName},</if>" +
            "<if test='course.courseCode != null'>course_code = #{course.courseCode},</if>" +
            "<if test='course.majorCode != null'>major_code = #{course.majorCode},</if>" +
            "<if test='course.stage != null'>stage = #{course.stage},</if>" +
            "<if test='course.trimester != null'>trimester = #{course.trimester},</if>" +
            "<if test='course.type != null'>type = #{course.type},</if>" +
            "<if test='course.day != null'>day = #{course.day},</if>" +
            "<if test='course.timeslot != null'>timeslot = #{course.timeslot},</if>" +
            "<if test='course.lecturerId != null'>lecturer_id = #{course.lecturerId}</if>" +
            "</set>" +
            "WHERE course_code = #{courseCode} AND type = #{course.type}" +
            "</script>")
    void updateCourse(@Param("courseCode") String courseCode, @Param("course") Course course);

    @Select("select * from course where lecturer_id = #{uid} and type = 'Lab'")
    List<Course> getCoursesLabListByUid(int uid);

    @Update("update course set lecturer_id = null where lecturer_id = #{uid}")
    void updateCourseLecturer(int uid);

    @Select("select count(*) from course where majorCode = #{major.majorCode} and stage = #{stage} and trimester = #{trimester} and " +
            "((day = #{day} and timeslot = #{timeslot}) or (day = #{day1} and timeslot = #{timeslot1}))")
    int timeConflict(@Param("major") Major major, @Param("stage")int stage, @Param("day")Day day,
                     @Param("timeslot")Timeslot timeslot, @Param("trimester") Trimester trimester, @Param("day1")Day day1, @Param("timeslot1")Timeslot timeslot1);


    @Select("select count(*) from course where course_code != #{courseCode} and majorCode = #{major.majorCode} and stage = #{stage} and trimester = #{trimester} and " +
            "((day = #{day} and timeslot = #{timeslot}) or (day = #{day1} and timeslot = #{timeslot1}))")
    int timeConflictWithoutCode(@Param("major") Major major, @Param("stage")int stage, @Param("day")Day day,
                                @Param("timeslot")Timeslot timeslot, @Param("trimester") Trimester trimester,
                                @Param("day1")Day day1, @Param("timeslot1")Timeslot timeslot1, @Param("courseCode") String courseCode);

    @Select("select * from course where cid = #{cid}")
    Course getCourseByCID(int cid);

    @Select("select * from course where course_code = #{courseCode} and type = 'Lab'")
    Course getCourseByCodeByLab(String courseCode);

    @Select("select * from course where majorCode = #{majorCode} and stage = #{stage} and type = 'Lab'")
    List<Course> getLabCoursesByMajorByStage(@Param("majorCode") String majorCode, @Param("stage") int i);

    @Select("select * from course where majorCode = #{majorCode} and stage = #{stage} and trimester = #{trimester}")
    List<Course> getCourseByMajorByStageByTrimester(@Param("majorCode") String majorCode, @Param("stage") int stage, @Param("trimester") Trimester trimester);

    @Select("select * from course where lecturer_id = #{uid} and trimester = #{trimester}")
    List<Course> getCoursesListByUidByTrimester(@Param("uid") int uid, @Param("trimester") Trimester trimester);

    @Select("select * from course where course_code = #{courseCode}")
    List<Course> getCourseByCode(String courseCode);
}
