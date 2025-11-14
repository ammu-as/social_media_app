package org.example.service;

import org.example.dao.SessionDao;
import org.example.model.Session;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Test
    void create_delegates_with_null_or_nonnull_expiry() throws Exception {
        // null expiresAt
        try (MockedConstruction<SessionDao> mocked = mockConstruction(SessionDao.class)) {
            SessionService svc = new SessionService();
            Session s = new Session();
            s.setSessionId("t");
            s.setUserId(1);
            s.setIpAddress("ip");
            s.setUserAgent("ua");
            s.setExpiresAt(null);
            svc.create(s);
            verify(mocked.constructed().get(0)).createSession(eq("t"), eq(1), isNull(), eq("ip"), eq("ua"));
        }
        // non-null expiresAt
        try (MockedConstruction<SessionDao> mocked = mockConstruction(SessionDao.class)) {
            SessionService svc = new SessionService();
            Session s = new Session();
            s.setSessionId("t");
            s.setUserId(1);
            s.setIpAddress("ip");
            s.setUserAgent("ua");
            s.setExpiresAt(LocalDateTime.now());
            svc.create(s);
            verify(mocked.constructed().get(0)).createSession(eq("t"), eq(1), any(), eq("ip"), eq("ua"));
        }
    }

    @Test
    void create_wraps_exception_and_invalidate_behaves() throws Exception {
        try (MockedConstruction<SessionDao> mocked = mockConstruction(SessionDao.class, (m, c) -> doThrow(new RuntimeException("x")).when(m).createSession(any(), anyInt(), any(), any(), any()))) {
            SessionService svc = new SessionService();
            assertThrows(RuntimeException.class, () -> svc.create(new Session()));
        }
        try (MockedConstruction<SessionDao> mocked = mockConstruction(SessionDao.class)) {
            SessionService svc = new SessionService();
            svc.invalidate("id");
            verify(mocked.constructed().get(0)).invalidate("id");
        }
        try (MockedConstruction<SessionDao> mocked = mockConstruction(SessionDao.class, (m, c) -> doThrow(new RuntimeException("x")).when(m).invalidate(any()))) {
            SessionService svc = new SessionService();
            assertThrows(RuntimeException.class, () -> svc.invalidate("id"));
        }
    }
}
