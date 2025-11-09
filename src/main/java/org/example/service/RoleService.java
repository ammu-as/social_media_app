package org.example.service;

import org.example.dao.RolesDao;

public class RoleService {
    private final RolesDao rolesDao = new RolesDao();

    public int createRole(String name) {
        try { return rolesDao.createRole(name); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}
