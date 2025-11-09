package org.example.service;

import org.example.dao.UserDao;
import org.example.model.user;

public class UserService {
    private final UserDao userDao = new UserDao();

    public int createUser(user user) {
        try {
            return userDao.create(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public user findByUsername(String username) {
        try { return userDao.findByUsername(username); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public user findByEmail(String email) {
        try { return userDao.findByEmail(email); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public user findById(int id) {
        try { return userDao.findById(id); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}
