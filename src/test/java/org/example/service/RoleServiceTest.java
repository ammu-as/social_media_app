package org.example.service;

import org.example.dao.RolesDao;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Test
    void createRole_delegates_and_returns() throws Exception {
        try (MockedConstruction<RolesDao> mocked = mockConstruction(RolesDao.class, (m, c) -> when(m.createRole("admin")).thenReturn(9))) {
            RoleService svc = new RoleService();
            assertEquals(9, svc.createRole("admin"));
            verify(mocked.constructed().get(0)).createRole("admin");
        }
    }

    @Test
    void createRole_wraps_exception() throws Exception {
        try (MockedConstruction<RolesDao> mocked = mockConstruction(RolesDao.class, (m, c) -> when(m.createRole(any())).thenThrow(new RuntimeException("x")))) {
            RoleService svc = new RoleService();
            assertThrows(RuntimeException.class, () -> svc.createRole("x"));
        }
    }
}
