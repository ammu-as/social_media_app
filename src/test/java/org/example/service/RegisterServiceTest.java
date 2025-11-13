package org.example.service;

import org.example.dao.RegisterDao;
import org.example.model.Register;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterServiceTest {

    @Test
    void record_delegates_to_dao() throws Exception {
        try (MockedConstruction<RegisterDao> mocked = mockConstruction(RegisterDao.class)) {
            RegisterService svc = new RegisterService();
            Register r = new Register();
            r.setUserId(2);
            r.setMethod("email");
            r.setIpAddress("ip");
            r.setReferrer("ref");
            svc.record(r);
            verify(mocked.constructed().get(0)).recordRegister(2, "email", "ip", "ref");
        }
    }

    @Test
    void record_wraps_exception() throws Exception {
        try (MockedConstruction<RegisterDao> mocked = mockConstruction(RegisterDao.class, (m, c) -> doThrow(new RuntimeException("x")).when(m).recordRegister(anyInt(), any(), any(), any()))) {
            RegisterService svc = new RegisterService();
            assertThrows(RuntimeException.class, () -> svc.record(new Register()));
        }
    }
}
