package org.example.service;

import org.example.model.Login;
import org.example.model.Register;
import org.example.model.Session;
import org.example.model.user;
import org.example.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Test
    void register_success_createsUser_and_recordsRegister() throws Exception {
        try (MockedConstruction<UserService> userSvcMock = mockConstruction(UserService.class, (mock, ctx) -> {
            when(mock.findByUsername("alice")).thenReturn(null);
            when(mock.findByEmail("a@b.com")).thenReturn(null);
            when(mock.createUser(any(user.class))).thenReturn(101);
        });
             MockedConstruction<RegisterService> regSvcMock = mockConstruction(RegisterService.class);
             MockedStatic<PasswordUtil> pwd = mockStatic(PasswordUtil.class)) {

            pwd.when(() -> PasswordUtil.hash("secret")).thenReturn("HASH");

            AuthService svc = new AuthService();
            int id = svc.register("alice", "a@b.com", "secret");
            assertEquals(101, id);

            UserService constructedUserSvc = userSvcMock.constructed().get(0);
            verify(constructedUserSvc).findByUsername("alice");
            verify(constructedUserSvc).findByEmail("a@b.com");
            verify(constructedUserSvc).createUser(any(user.class));

            RegisterService constructedRegSvc = regSvcMock.constructed().get(0);
            verify(constructedRegSvc, times(1)).record(any(Register.class));
        }
    }

    @Test
    void register_throws_on_taken_username_or_email() throws Exception {
        // username taken
        try (MockedConstruction<UserService> userSvcMock = mockConstruction(UserService.class, (mock, ctx) -> {
            when(mock.findByUsername("alice")).thenReturn(new user());
        })) {
            AuthService svc = new AuthService();
            assertThrows(Exception.class, () -> svc.register("alice", "a@b.com", "p"));
        }
        // email taken
        try (MockedConstruction<UserService> userSvcMock = mockConstruction(UserService.class, (mock, ctx) -> {
            when(mock.findByUsername("alice")).thenReturn(null);
            when(mock.findByEmail("a@b.com")).thenReturn(new user());
        })) {
            AuthService svc = new AuthService();
            assertThrows(Exception.class, () -> svc.register("alice", "a@b.com", "p"));
        }
    }

    @Test
    void login_success_records_and_creates_session() throws Exception {
        user u = new user();
        u.setUserId(5);
        u.setHashPassword("HASH");
        try (MockedConstruction<UserService> userSvcMock = mockConstruction(UserService.class, (mock, ctx) -> {
            when(mock.findByUsername("alice")).thenReturn(null);
            when(mock.findByEmail("alice")).thenReturn(u);
        });
             MockedConstruction<LoginService> loginSvcMock = mockConstruction(LoginService.class);
             MockedConstruction<SessionService> sessionSvcMock = mockConstruction(SessionService.class);
             MockedStatic<PasswordUtil> pwd = mockStatic(PasswordUtil.class)) {

            pwd.when(() -> PasswordUtil.verify("pw", "HASH")).thenReturn(true);

            AuthService svc = new AuthService();
            String token = svc.login("alice", "pw", "127.0.0.1", "UA");
            assertNotNull(token);

            // verify interactions
            LoginService loginConstructed = loginSvcMock.constructed().get(0);
            verify(loginConstructed, times(2)).record(any(Login.class));

            SessionService sessionConstructed = sessionSvcMock.constructed().get(0);
            verify(sessionConstructed, times(1)).create(any(Session.class));
        }
    }

    @Test
    void login_fails_when_no_user_or_bad_password() throws Exception {
        // no user found
        try (MockedConstruction<UserService> userSvcMock = mockConstruction(UserService.class, (mock, ctx) -> {
            when(mock.findByUsername("x")).thenReturn(null);
            when(mock.findByEmail("x")).thenReturn(null);
        });
             MockedConstruction<LoginService> loginSvcMock = mockConstruction(LoginService.class)) {
            AuthService svc = new AuthService();
            assertThrows(Exception.class, () -> svc.login("x", "p", "ip", "ua"));
            verify(loginSvcMock.constructed().get(0), times(1)).record(any(Login.class));
        }
        // bad password
        user u = new user();
        u.setUserId(5);
        u.setHashPassword("HASH");
        try (MockedConstruction<UserService> userSvcMock = mockConstruction(UserService.class, (mock, ctx) -> {
            when(mock.findByUsername("a")).thenReturn(u);
        });
             MockedConstruction<LoginService> loginSvcMock = mockConstruction(LoginService.class);
             MockedStatic<PasswordUtil> pwd = mockStatic(PasswordUtil.class)) {
            pwd.when(() -> PasswordUtil.verify("bad", "HASH")).thenReturn(false);
            AuthService svc = new AuthService();
            assertThrows(Exception.class, () -> svc.login("a", "bad", "ip", "ua"));
            verify(loginSvcMock.constructed().get(0), times(1)).record(any(Login.class));
        }
    }
}
