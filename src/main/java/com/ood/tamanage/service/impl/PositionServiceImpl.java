package com.ood.tamanage.service.impl;

import com.ood.tamanage.enums.Evaluation;
import com.ood.tamanage.mapper.PositionMapper;
import com.ood.tamanage.pojo.Positions;
import com.ood.tamanage.service.PositionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Resource
    private PositionMapper positionMapper;
    @Override
    public int getPositionsCount() {
        return positionMapper.getPositionsCount();
    }

    @Override
    public int getPositionsCountByCid(int classId) {
        return positionMapper.getPositionsCountByCid(classId);
    }

    @Override
    public boolean canAddPositionByCid(int cid, int quantity) {
        return positionMapper.getPositionsCountByCid(cid) < quantity;
    }

    @Override
    public boolean canAddPositionByMid(int classId, int quantity) {
        return positionMapper.getPositionsCountByMid(classId) < quantity;
    }

    @Override
    public void addPositionByCid(int uid, int cid) {
        positionMapper.addPositionByCid(uid, cid);
    }

    @Override
    public void addPositionByMid(int uid, int mid) {
        positionMapper.addPositionByMid(uid, mid);
    }

    @Override
    public List<Positions> getPositionsList() {
        return positionMapper.getPositionsList();
    }

    @Override
    public void updatePosition(int pid, Evaluation evaluation1) {
        positionMapper.updatePosition(pid, evaluation1);
    }

    @Override
    public void deletePositionByCourseId(int cid) {
        positionMapper.deletePositionByCourseId(cid);
    }

    @Override
    public void deletePositionByModuleId(int mid) {
        positionMapper.deletePositionByModuleId(mid);
    }
}
