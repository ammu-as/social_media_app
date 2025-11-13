package org.example.service;

import org.example.dao.UserDao;
import org.example.model.user;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Test
    void createUser_returnsGeneratedId() throws Exception {
        try (MockedConstruction<UserDao> mocked = mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.create(any(user.class))).thenReturn(42);
        })) {
            UserService service = new UserService();
            user u = new user();
            u.setUsername("alice");
            int id = service.createUser(u);
            assertEquals(42, id);
            UserDao constructed = mocked.constructed().get(0);
            verify(constructed, times(1)).create(any(user.class));
        }
    }

    @Test
    void createUser_wrapsDaoException() throws Exception {
        try (MockedConstruction<UserDao> mocked = mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.create(any(user.class))).thenThrow(new SQLException("db down"));
        })) {
            UserService service = new UserService();
            assertThrows(RuntimeException.class, () -> service.createUser(new user()));
        }
    }

    @Test
    void findByUsername_success() throws Exception {
        user expected = new user();
        expected.setUsername("bob");
        try (MockedConstruction<UserDao> mocked = mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.findByUsername(eq("bob"))).thenReturn(expected);
        })) {
            UserService service = new UserService();
            user actual = service.findByUsername("bob");
            assertSame(expected, actual);
        }
    }

    @Test
    void findByUsername_wrapsDaoException() throws Exception {
        try (MockedConstruction<UserDao> mocked = mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.findByUsername(anyString())).thenThrow(new SQLException("boom"));
        })) {
            UserService service = new UserService();
            assertThrows(RuntimeException.class, () -> service.findByUsername("x"));
        }
    }

    @Test
    void findByEmail_success() throws Exception {
        user expected = new user();
        expected.setMailId("a@b.com");
        try (MockedConstruction<UserDao> mocked = mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.findByEmail(eq("a@b.com"))).thenReturn(expected);
        })) {
            UserService service = new UserService();
            user actual = service.findByEmail("a@b.com");
            assertSame(expected, actual);
        }
    }

    @Test
    void findByEmail_wrapsDaoException() throws Exception {
        try (MockedConstruction<UserDao> mocked = mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.findByEmail(anyString())).thenThrow(new SQLException("boom"));
        })) {
            UserService service = new UserService();
            assertThrows(RuntimeException.class, () -> service.findByEmail("x@x"));
        }
    }

    @Test
    void findById_success() throws Exception {
        user expected = new user();
        expected.setUserId(7);
        try (MockedConstruction<UserDao> mocked = mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.findById(eq(7))).thenReturn(expected);
        })) {
            UserService service = new UserService();
            user actual = service.findById(7);
            assertSame(expected, actual);
        }
    }

    @Test
    void findById_wrapsDaoException() throws Exception {
        try (MockedConstruction<UserDao> mocked = mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.findById(anyInt())).thenThrow(new SQLException("boom"));
        })) {
            UserService service = new UserService();
            assertThrows(RuntimeException.class, () -> service.findById(1));
        }
    }
}
