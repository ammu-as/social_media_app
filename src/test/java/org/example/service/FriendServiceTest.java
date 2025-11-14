package org.example.service;

import org.example.dao.FriendDao;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FriendServiceTest {

    @Test
    void follow_delegates_and_returns() throws Exception {
        try (MockedConstruction<FriendDao> mocked = mockConstruction(FriendDao.class, (m, c) -> when(m.follow(1, 2)).thenReturn(true))) {
            FriendService svc = new FriendService();
            assertTrue(svc.follow(1, 2));
            verify(mocked.constructed().get(0)).follow(1, 2);
        }
    }

    @Test
    void follow_wraps_exception() throws Exception {
        try (MockedConstruction<FriendDao> mocked = mockConstruction(FriendDao.class, (m, c) -> when(m.follow(anyInt(), anyInt())).thenThrow(new RuntimeException("db")))) {
            FriendService svc = new FriendService();
            assertThrows(RuntimeException.class, () -> svc.follow(1, 2));
        }
    }

    @Test
    void unfollow_delegates_and_wraps_exception() throws Exception {
        try (MockedConstruction<FriendDao> mocked = mockConstruction(FriendDao.class)) {
            FriendService svc = new FriendService();
            svc.unfollow(1, 2);
            verify(mocked.constructed().get(0)).unfollow(1, 2);
        }
        try (MockedConstruction<FriendDao> mocked = mockConstruction(FriendDao.class, (m, c) -> doThrow(new RuntimeException("x")).when(m).unfollow(anyInt(), anyInt()))) {
            FriendService svc = new FriendService();
            assertThrows(RuntimeException.class, () -> svc.unfollow(1, 2));
        }
    }
}
