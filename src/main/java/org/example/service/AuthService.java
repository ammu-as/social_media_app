package org.example.service;

import org.example.model.Login;
import org.example.model.user;
import org.example.util.PasswordUtil;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;

public class AuthService {
    private final UserService userService = new UserService();
    private final RegisterService registerService = new RegisterService();
    private final LoginService loginService = new LoginService();
    private final SessionService sessionService = new SessionService();

    public int register(String username, String email, String password) throws Exception {
        if (userService.findByUsername(username) != null) throw new Exception("username taken");
        if (userService.findByEmail(email) != null) throw new Exception("email taken");
        user u = new user();
        u.setUsername(username);
        u.setMailId(email);
        u.setHashPassword(PasswordUtil.hash(password));
        int userId = userService.createUser(u);
        // record registration event
        org.example.model.Register reg = new org.example.model.Register();
        reg.setUserId(userId);
        reg.setMethod("email");
        registerService.record(reg);
        return userId;
    }

    public String login(String usernameOrEmail, String password, String ip, String ua) throws Exception {
        user u = userService.findByUsername(usernameOrEmail);
        if (u == null) u = userService.findByEmail(usernameOrEmail);
        if (u == null) {
            loginService.record(createLogin(null, usernameOrEmail, ip, ua, "FAILED", "no_user"));
            throw new Exception("Invalid credentials");
        }
        if (!PasswordUtil.verify(password, u.getHashPassword())) {
            loginService.record(createLogin(u.getUserId(), usernameOrEmail, ip, ua, "FAILED", "bad_password"));
            throw new Exception("Invalid credentials");
        }
        String token = generateToken();
        // create session
        org.example.model.Session s = new org.example.model.Session();
        s.setSessionId(token);
        s.setUserId(u.getUserId());
        s.setIpAddress(ip);
        s.setUserAgent(ua);
        s.setActive(true);
        sessionService.create(s);
        loginService.record(createLogin(u.getUserId(), usernameOrEmail, ip, ua, "SUCCESS", null));
        return token;
    }

    private Login createLogin(Integer userId, String usernameAttempted, String ip, String ua, String status, String reason) {
        Login l = new Login();
        l.setUserId(userId);
        l.setUsernameAttempted(usernameAttempted);
        l.setIpAddress(ip);
        l.setUserAgent(ua);
        l.setLoginStatus(status);
        l.setReason(reason);
        return l;
    }

    private String generateToken() {
        SecureRandom rnd = new SecureRandom();
        byte[] b = new byte[48];
        rnd.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }
}
