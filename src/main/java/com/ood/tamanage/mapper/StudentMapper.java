package com.ood.tamanage.mapper;

import com.ood.tamanage.pojo.Positions;
import com.ood.tamanage.pojo.Student;
import com.ood.tamanage.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface StudentMapper {

    @Select("select * from student")
    List<Student> getStudentsList();

    @Select("select * from student where uid = #{uid}")
    Student getStudentById(int uid);

    @Select("select * from student where student_id = #{studentId}")
    Student getStudentByStudentId(String studentId);

    @Insert("insert into student values (null, #{username}, #{password}, #{email}, #{studentId}, " +
            "#{educationStatus}, #{stage}, #{majorCode}, #{timeLimitation})")
    void addStudent(Student student);
    @Update("<script>" +
            "UPDATE student" +
            "<set>" +
            "<if test='username != null'>username = #{username},</if>" +
            "<if test='password != null'>password = #{password},</if>" +
            "<if test='email != null'>email = #{email},</if>" +
            "<if test='educationStatus != null'>educationStatus = #{educationStatus},</if>" +
            "<if test='stage != null'>stage = #{stage},</if>" +
            "<if test='majorCode != null'>majorCode = #{majorCode},</if>" +
            "<if test='timeLimitation != null'>time_limitation = #{timeLimitation}</if>" +
            "</set>" +
            "WHERE uid = #{uid} or student_id = #{studentId}" +
            "</script>")
    void updateStudent(Student student);

    @Delete("delete from student where uid = #{uid}")
    void deleteStudent(int uid);

    @Select("select * from student where student_id = #{username} and password = #{password}")
    Student validate(User user);

    @Select("select count(*) from applications where uid = #{uid} and cid = #{cid}")
    int applyForCourse(@Param("uid") int uid, @Param("cid") int classId);

    @Select("select count(*) from applications where uid = #{uid} and cid is null and mid = #{mid}")
    int applyForModule(@Param("uid") int uid, @Param("mid") int classId);


    @Select("select * from positions where uid = #{uid}")
    List<Positions> getPositionsListByUid(int uid);


}
