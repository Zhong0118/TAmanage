package com.ood.tamanage.service;

import com.ood.tamanage.pojo.Admin;

import java.util.List;

public interface AdminService extends UserService {
    List<Admin> getAdminsList();

    Admin getAdminById(int uid);

    void addAdmin(Admin admin);

    void updateAdmin(Admin admin);

    void deleteAdmin(int uid);
}
