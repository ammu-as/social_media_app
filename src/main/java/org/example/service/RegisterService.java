package org.example.service;

import org.example.dao.RegisterDao;
import org.example.model.Register;

public class RegisterService {
    private final RegisterDao registerDao = new RegisterDao();

    public void record(Register reg) {
        try {
            registerDao.recordRegister(reg.getUserId(), reg.getMethod(), reg.getIpAddress(), reg.getReferrer());
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
