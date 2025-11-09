package org.example.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.config.MongoProvider;

public class ConversationRepo {
    private final MongoCollection<Document> convs;

    public ConversationRepo() {
        MongoDatabase db = MongoProvider.getDatabase();
        convs = db.getCollection("conversations");
    }

    public Document createConversation(Document d) {
        convs.insertOne(d);
        return d;
    }
}
