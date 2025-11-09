package org.example.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.example.config.MongoProvider;

import java.util.ArrayList;
import java.util.List;

public class PostRepo {
    private final MongoCollection<Document> posts;

    public PostRepo() {
        MongoDatabase db = MongoProvider.getDatabase();
        posts = db.getCollection("posts");
        posts.createIndex(Indexes.descending("createdAt"));
        posts.createIndex(Indexes.ascending("userId", "createdAt"));
    }

    public Document insertPost(Document p) {
        posts.insertOne(p);
        return p;
    }

    public List<Document> findRecent(int limit) {
        List<Document> out = new ArrayList<>();
        posts.find().sort(new Document("createdAt", -1)).limit(limit).into(out);
        return out;
    }
}
