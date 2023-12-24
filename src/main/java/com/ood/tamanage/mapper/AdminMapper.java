package com.ood.tamanage.mapper;

import com.ood.tamanage.pojo.Admin;
import com.ood.tamanage.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AdminMapper {
    @Select("select * from admin")
    List<Admin> getAdminsList();

    @Select("select * from admin where uid = #{uid}")
    Admin getAdminById(int uid);

    @Insert("insert into admin values (null, #{username}, #{password}, #{email})")
    void addAdmin(Admin admin);
    @Update("<script>" +
            "UPDATE admin" +
            "<set>" +
            "<if test='username != null'>username = #{username},</if>" +
            "<if test='password != null'>password = #{password},</if>" +
            "<if test='email != null'>email = #{email},</if>" +
            "</set>" +
            "WHERE uid = #{uid}" +
            "</script>")
    void updateAdmin(Admin admin);

    @Delete("delete from admin where uid = #{uid}")
    void deleteAdmin(int uid);

    @Select("select * from admin where username = #{username} and password = #{password}")
    Admin validate(User user);
}
