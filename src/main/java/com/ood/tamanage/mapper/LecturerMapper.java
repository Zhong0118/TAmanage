package com.ood.tamanage.mapper;

import com.ood.tamanage.enums.Day;
import com.ood.tamanage.enums.Timeslot;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.pojo.Lecturer;
import com.ood.tamanage.pojo.Major;
import com.ood.tamanage.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface LecturerMapper {
    @Select("select * from Lecturer")
    List<Lecturer> getLecturersList();

    @Select("select * from lecturer where uid = #{uid}")
    Lecturer getLecturerById(int uid);

    @Insert("insert into Lecturer values (null, #{username}, #{password}, #{email})")
    void addLecturer(Lecturer Lecturer);

    @Update("<script>" +
            "UPDATE lecturer" +
            "<set>" +
            "<if test='username != null'>username = #{username},</if>" +
            "<if test='password != null'>password = #{password},</if>" +
            "<if test='email != null'>email = #{email},</if>" +
            "</set>" +
            "WHERE uid = #{uid}" +
            "</script>")
    void updateLecturer(Lecturer Lecturer);

    @Delete("delete from Lecturer where uid = #{uid}")
    void deleteLecturer(int uid);

    @Select("select * from lecturer where username = #{username} and password = #{password}")
    Lecturer validate(User user);

    @Select("select count(*) from course where lecturer_id = #{lecturer.uid} and trimester = #{trimester} " +
            "and ((day = #{day1} and timeslot = #{timeslot1}) or (day = #{day} and timeslot = #{timeslot}))")
    int timeConflict1(@Param("lecturer") Lecturer lecturer, @Param("day")Day day, @Param("timeslot")Timeslot timeslot,
                      @Param("trimester") Trimester trimester, @Param("day1")Day day1, @Param("timeslot1")Timeslot timeslot1);

    @Select("select count(*) from module where lecturer_id = #{lecturer.uid} and trimester = #{trimester} " +
            "and ((day = #{day} and timeslot = #{timeslot}) or (day = #{day1} and timeslot = #{timeslot1}))")
    int timeConflict2(@Param("lecturer") Lecturer lecturer, @Param("day")Day day, @Param("timeslot")Timeslot timeslot,
                      @Param("trimester") Trimester trimester, @Param("day1")Day day1, @Param("timeslot1")Timeslot timeslot1);

    @Select("select count(*) from course where course_code != #{courseCode} and lecturer_id = #{lecturer.uid} and trimester = #{trimester} " +
            "and ((day = #{day1} and timeslot = #{timeslot1}) or (day = #{day} and timeslot = #{timeslot}))")
    int timeConflictWithoutCode1(@Param("lecturer") Lecturer lecturer, @Param("day")Day day, @Param("timeslot")Timeslot timeslot,
                                 @Param("trimester") Trimester trimester, @Param("day1")Day day1, @Param("timeslot1")Timeslot timeslot1, @Param("courseCode") String courseCode);
    @Select("select count(*) from module where module_code != #{courseCode} and lecturer_id = #{lecturer.uid} and trimester = #{trimester} " +
            "and ((day = #{day1} and timeslot = #{timeslot1}) or (day = #{day} and timeslot = #{timeslot}))")
    int timeConflictWithoutCode2(@Param("lecturer") Lecturer lecturer, @Param("day")Day day, @Param("timeslot")Timeslot timeslot,
                                 @Param("trimester") Trimester trimester, @Param("day1")Day day1, @Param("timeslot1")Timeslot timeslot1, @Param("courseCode") String courseCode);

}
