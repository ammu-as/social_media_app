package org.example.service;

import org.bson.Document;
import org.example.mongo.CommentRepo;
import org.example.mongo.LikeRepo;
import org.example.mongo.PostRepo;

import java.util.List;

public class SocialService {
    private final PostRepo postRepo = new PostRepo();
    private final CommentRepo commentRepo = new CommentRepo();
    private final LikeRepo likeRepo = new LikeRepo();

    public Document createPost(Document p) {
        return postRepo.insertPost(p);
    }

    public List<Document> getFeed(int limit) {
        return postRepo.findRecent(limit);
    }

    public List<Document> getFeedByAuthors(List<Integer> userIds, int limit) {
        return postRepo.findByAuthors(userIds, limit);
    }

    public void addComment(Document c) {
        commentRepo.insertComment(c);
    }

    public boolean addLike(Document l) {
        return likeRepo.insertLike(l);
    }

    public void removeLike(Document filter) {
        likeRepo.removeLike(filter);
    }

    public long countLikesForPost(org.bson.types.ObjectId postId) {
        return likeRepo.countLikesForPost(postId);
    }

    public long countCommentsForPost(org.bson.types.ObjectId postId) {
        return commentRepo.countByPost(postId);
    }
}
