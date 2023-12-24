package com.ood.tamanage.mapper;

import com.ood.tamanage.enums.Evaluation;
import com.ood.tamanage.pojo.Positions;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PositionMapper {

    @Select("SELECT COUNT(*) FROM positions")
    int getPositionsCount();

    @Select("SELECT COUNT(*) FROM positions WHERE cid = #{classId}")
    int getPositionsCountByCid(int classId);

    @Select("SELECT COUNT(*) FROM positions WHERE mid = #{classId}")
    int getPositionsCountByMid(int classId);

    @Insert("INSERT INTO positions VALUES (null, #{uid}, #{cid}, null, null)")
    void addPositionByCid(@Param("uid") int uid, @Param("cid")int cid);

    @Insert("INSERT INTO positions VALUES (null, #{uid}, null, #{mid}, null)")
    void addPositionByMid(@Param("uid")int uid, @Param("mid")int mid);

    @Select("SELECT * FROM positions")
    List<Positions> getPositionsList();

    @Update("UPDATE positions SET evaluation = #{evaluation} WHERE pid = #{pid}")
    void updatePosition(@Param("pid") int pid, @Param("evaluation") Evaluation evaluation1);

    @Delete("DELETE FROM positions WHERE cid = #{cid}")
    void deletePositionByCourseId(int cid);

    @Delete("DELETE FROM positions WHERE mid = #{mid}")
    void deletePositionByModuleId(int mid);
}
