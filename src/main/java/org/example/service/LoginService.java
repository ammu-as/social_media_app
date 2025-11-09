package org.example.service;

import org.example.dao.LoginDao;
import org.example.model.Login;

public class LoginService {
    private final LoginDao loginDao = new LoginDao();

    public void record(Login login) {
        try {
            loginDao.recordLogin(login.getUserId(), login.getUsernameAttempted(), login.getIpAddress(), login.getUserAgent(), login.getLoginStatus(), login.getReason());
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
