package org.example.service;

import org.bson.Document;
import org.example.mongo.PostRepo;
import org.example.mongo.CommentRepo;
import org.example.mongo.LikeRepo;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SocialServiceTest {

    @Test
    void createPost_and_getFeed_delegate() {
        Document p = new Document("a", 1);
        List<Document> feed = Arrays.asList(new Document("x", 1));
        try (MockedConstruction<PostRepo> post = mockConstruction(PostRepo.class, (m, c) -> {
                 when(m.insertPost(p)).thenReturn(p);
                 when(m.findRecent(10)).thenReturn(feed);
             })) {
            SocialService svc = new SocialService();
            assertSame(p, svc.createPost(p));
            assertEquals(feed, svc.getFeed(10));
            verify(post.constructed().get(0)).insertPost(p);
            verify(post.constructed().get(0)).findRecent(10);
        }
    }

    @Test
    void comments_and_likes_delegate() {
        Document c = new Document("c", 1);
        Document l = new Document("l", 1);
        Document f = new Document("f", 1);
        try (MockedConstruction<CommentRepo> comments = mockConstruction(CommentRepo.class);
             MockedConstruction<LikeRepo> likes = mockConstruction(LikeRepo.class, (m, cst) -> when(m.insertLike(l)).thenReturn(true))) {
            SocialService svc = new SocialService();
            svc.addComment(c);
            assertTrue(svc.addLike(l));
            svc.removeLike(f);
            verify(comments.constructed().get(0)).insertComment(c);
            verify(likes.constructed().get(0)).insertLike(l);
            verify(likes.constructed().get(0)).removeLike(f);
        }
    }
}
