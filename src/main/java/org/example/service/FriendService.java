package org.example.service;

import org.example.dao.FriendDao;

public class FriendService {
    private final FriendDao friendsDao = new FriendDao();

    public boolean follow(int followerId, int followeeId) {
        try {
            return friendsDao.follow(followerId, followeeId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void unfollow(int followerId, int followeeId) {
        try {
            friendsDao.unfollow(followerId, followeeId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public java.util.List<Integer> listFollowees(int followerId) {
        try {
            return friendsDao.findFollowees(followerId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
