package org.example.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.config.MongoProvider;

public class LikeRepo {
    private final MongoCollection<Document> likes;

    public LikeRepo() {
        MongoDatabase db = MongoProvider.getDatabase();
        likes = db.getCollection("likes");
        likes.createIndex(Indexes.compoundIndex(Indexes.ascending("targetType"), Indexes.ascending("targetId"), Indexes.ascending("userId")),
                new IndexOptions().unique(true));
    }

    public boolean insertLike(Document d) {
        try {
            likes.insertOne(d);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void removeLike(Document filter) {
        likes.deleteOne(filter);
    }

    public long countLikesForPost(ObjectId postId) {
        Document filter = new Document("targetType", "post").append("targetId", postId);
        return likes.countDocuments(filter);
    }
}
