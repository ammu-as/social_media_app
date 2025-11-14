package org.example.service;

import org.example.dao.PasswordResetDao;
import org.example.dao.UserDao;
import org.example.model.user;
import org.example.util.PasswordUtil;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordResetService {
    private final PasswordResetDao resetDao = new PasswordResetDao();
    private final UserDao userDao = new UserDao();
    private final UserService userService = new UserService();

    public String requestReset(String usernameOrEmail) {
        try {
            user u = userService.findByUsername(usernameOrEmail);
            if (u == null) u = userService.findByEmail(usernameOrEmail);
            if (u == null) throw new RuntimeException("user not found");
            String token = generateToken();
            resetDao.createToken(u.getUserId(), token);
            return token;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resetWithToken(String token, String newPassword) {
        try {
            Integer userId = resetDao.getValidUserIdByToken(token);
            if (userId == null) throw new RuntimeException("invalid or expired token");
            String hash = PasswordUtil.hash(newPassword);
            userDao.updatePassword(userId, hash);
            resetDao.deleteToken(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateToken() {
        SecureRandom rnd = new SecureRandom();
        byte[] b = new byte[32];
        rnd.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }
}
