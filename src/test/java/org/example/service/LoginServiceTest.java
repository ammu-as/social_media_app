package org.example.service;

import org.example.dao.LoginDao;
import org.example.model.Login;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Test
    void record_delegates_to_dao() throws Exception {
        try (MockedConstruction<LoginDao> mocked = mockConstruction(LoginDao.class)) {
            LoginService svc = new LoginService();
            Login l = new Login();
            l.setUserId(1);
            l.setUsernameAttempted("u");
            l.setIpAddress("ip");
            l.setUserAgent("ua");
            l.setLoginStatus("S");
            l.setReason(null);
            svc.record(l);
            verify(mocked.constructed().get(0)).recordLogin(1, "u", "ip", "ua", "S", null);
        }
    }

    @Test
    void record_wraps_exception() {
        try (MockedConstruction<LoginDao> mocked = mockConstruction(LoginDao.class, (m, c) -> doThrow(new RuntimeException("x")).when(m).recordLogin(any(), any(), any(), any(), any(), any()))) {
            LoginService svc = new LoginService();
            assertThrows(RuntimeException.class, () -> svc.record(new Login()));
        }
    }
}
