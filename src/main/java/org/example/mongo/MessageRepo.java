package org.example.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.config.MongoProvider;

import java.util.ArrayList;
import java.util.List;

public class MessageRepo {
    private final MongoCollection<Document> messages;

    public MessageRepo() {
        MongoDatabase db = MongoProvider.getDatabase();
        messages = db.getCollection("messages");
    }

    public void addMessage(Document d) { messages.insertOne(d); }

    public List<Document> getMessages(Object conversationId, int limit) {
        List<Document> out = new ArrayList<>();
        messages.find(new Document("conversationId", conversationId)).sort(new Document("createdAt", -1)).limit(limit).into(out);
        return out;
    }
}
