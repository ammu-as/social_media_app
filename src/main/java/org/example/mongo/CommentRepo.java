package org.example.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.config.MongoProvider;

import java.util.ArrayList;
import java.util.List;

public class CommentRepo {
    private final MongoCollection<Document> comments;

    public CommentRepo() {
        MongoDatabase db = MongoProvider.getDatabase();
        comments = db.getCollection("comments");
    }

    public void insertComment(Document c) { comments.insertOne(c); }

    public List<Document> findByPost(Object postId) {
        List<Document> out = new ArrayList<>();
        comments.find(new Document("postId", postId)).sort(new Document("createdAt", 1)).into(out);
        return out;
    }
}
