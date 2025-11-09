package org.example.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoProvider {
    private static final MongoClient client;
    private static final MongoDatabase db;
    static {
        String uri = System.getenv().getOrDefault("MONGO_URI", "mongodb://localhost:27017");
        client = MongoClients.create(uri);
        db = client.getDatabase(System.getenv().getOrDefault("MONGO_DB", "social_mediaa"));
    }
    public static MongoDatabase getDatabase() { return db; }
    public static void close() { client.close(); }
}

