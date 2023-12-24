package com.ood.tamanage.service;

import com.ood.tamanage.enums.Evaluation;
import com.ood.tamanage.pojo.Positions;

import java.util.List;

public interface PositionService {
    int getPositionsCount();

    int getPositionsCountByCid(int classId);
    boolean canAddPositionByCid(int cid, int quantity);

    boolean canAddPositionByMid(int classId, int quantity);

    void addPositionByCid(int uid, int cid);

    void addPositionByMid(int uid, int mid);

    List<Positions> getPositionsList();

    void updatePosition(int pid, Evaluation evaluation1);

    void deletePositionByCourseId(int cid);

    void deletePositionByModuleId(int mid);
}
