package com.ood.tamanage.mapper;

import com.ood.tamanage.enums.Purpose;
import com.ood.tamanage.enums.Status;
import com.ood.tamanage.pojo.ClassModify;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ClassModifyMapper {

    @Select("SELECT * FROM classmodify WHERE studentId = #{uid}")
    List<ClassModify> getAllByStudent(Integer uid);

    @Insert("INSERT INTO classmodify values (null, #{purpose}, #{status}, #{cid}, null, #{uid}, null)")
    void addCourseModify(@Param("purpose") Purpose purpose, @Param("status") Status status, @Param("cid") int cid, @Param("uid") int uid);

    @Select("SELECT * FROM classmodify WHERE status = 'Pending'")
    List<ClassModify> getAllByPending();

    @Select("SELECT * FROM classmodify WHERE status != 'Pending'")
    List<ClassModify> getAllWithoutPending();

    @Update("UPDATE classmodify SET status = #{status} WHERE studentId = #{uid} AND cid = #{cid}")
    void updateCourseModify(@Param("uid") int uid, @Param("cid") int cid, @Param("status") Status status);

    @Delete("DELETE FROM classmodify WHERE cid = #{cid}")
    void deleteCourseModifyByCid(int cid);

    @Select("SELECT count(*) FROM classmodify WHERE studentId = #{uid} AND cid = #{cid}")
    int hasModify(@Param("uid") int uid, @Param("cid") int cid);
}
