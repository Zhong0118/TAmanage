package com.ood.tamanage.service.impl;

import com.ood.tamanage.mapper.AdminMapper;
import com.ood.tamanage.mapper.StudentMapper;
import com.ood.tamanage.pojo.Admin;
import com.ood.tamanage.pojo.Result;
import com.ood.tamanage.pojo.User;
import com.ood.tamanage.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public List<Admin> getAdminsList() {
        return adminMapper.getAdminsList();
    }

    @Override
    public Admin getAdminById(int uid) {
        return adminMapper.getAdminById(uid);
    }

    @Override
    public void addAdmin(Admin admin) {
        adminMapper.addAdmin(admin);
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminMapper.updateAdmin(admin);
    }

    @Override
    public void deleteAdmin(int uid) {
        adminMapper.deleteAdmin(uid);
    }

    @Override
    public User validate(User user) {
        return adminMapper.validate(user);
    }
}
