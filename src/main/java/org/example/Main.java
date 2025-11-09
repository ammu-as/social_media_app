package org.example;

import org.bson.Document;
import org.example.config.MongoProvider;
import org.example.service.AuthService;
import org.example.service.FriendService;
import org.example.service.SocialService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        AuthService auth = new AuthService();
        SocialService social = new SocialService();
        FriendService friendService = new FriendService();
        Scanner sc = new Scanner(System.in);
        String currentToken = null;
        int currentUserId = -1;

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("3) Create Post");
            System.out.println("4) View Feed");
            System.out.println("5) Follow user");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            String opt = sc.nextLine();
            if (opt.equals("0")) break;
            try {
                switch (opt) {
                    case "1" -> {
                        System.out.print("username: "); String u = sc.nextLine();
                        System.out.print("email: "); String e = sc.nextLine();
                        System.out.print("password: "); String p = sc.nextLine();
                        int id = auth.register(u, e, p);
                        System.out.println("Registered user id: " + id);
                    }
                    case "2" -> {
                        System.out.print("username/email: "); String uname = sc.nextLine();
                        System.out.print("password: "); String pwd = sc.nextLine();
                        String token = auth.login(uname, pwd, "127.0.0.1", "cli");
                        System.out.println("Login success. token: " + token);
                        currentToken = token;
                    }
                    case "3" -> {
                        if (currentToken == null) { System.out.println("Login first."); break; }
                        System.out.print("author userId (int): "); int uid = Integer.parseInt(sc.nextLine());
                        System.out.print("username: "); String un = sc.nextLine();
                        System.out.print("content: "); String content = sc.nextLine();
                        Document p = new Document()
                                .append("userId", uid)
                                .append("username", un)
                                .append("content", content)
                                .append("createdAt", new Date());
                        social.createPost(p);
                        System.out.println("Post created.");
                    }
                    case "4" -> {
                        List<Document> posts = social.getFeed(10);
                        System.out.println("Feed:");
                        for (Document d : posts) {
                            System.out.printf("id=%s userId=%s user=%s time=%s\n  %s\n",
                                    d.getObjectId("_id"), d.get("userId"), d.getString("username"), d.getDate("createdAt"), d.getString("content"));
                        }
                    }
                    case "5" -> {
                        System.out.print("your id: "); int follower = Integer.parseInt(sc.nextLine());
                        System.out.print("follow user id: "); int followee = Integer.parseInt(sc.nextLine());
                        boolean ok = friendService.follow(follower, followee);
                        System.out.println(ok ? "Now following" : "Already following / error");
                    }
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        MongoProvider.close();
        System.out.println("bye");
    }
}
