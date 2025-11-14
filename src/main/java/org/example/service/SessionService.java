package org.example.service;

import org.example.dao.SessionDao;
import org.example.model.Session;

import java.sql.Timestamp;

public class SessionService {
    private final SessionDao sessionsDao = new SessionDao();

    public void create(Session s) {
        try {
            Timestamp expires = s.getExpiresAt() == null ? null : Timestamp.valueOf(s.getExpiresAt());
            sessionsDao.createSession(s.getSessionId(), s.getUserId(), expires, s.getIpAddress(), s.getUserAgent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void invalidate(String sessionId) {
        try {
            sessionsDao.invalidate(sessionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getUserId(String sessionId) {
        try {
            return sessionsDao.getUserIdBySession(sessionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
