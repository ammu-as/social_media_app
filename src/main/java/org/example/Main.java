package org.example;

import org.bson.Document;
import org.example.config.MongoProvider;
import org.example.service.AuthService;
import org.example.service.FriendService;
import org.example.service.SocialService;
import org.example.service.SessionService;
import org.example.service.UserService;
import org.example.service.PasswordResetService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        AuthService auth = new AuthService();
        SocialService social = new SocialService();
        FriendService friendService = new FriendService();
        SessionService sessionService = new SessionService();
        UserService userService = new UserService();
        PasswordResetService resetService = new PasswordResetService();
        Scanner sc = new Scanner(System.in);
        String currentToken = null;
        int currentUserId = -1;
        String currentUsername = null;

        printBanner();
        while (true) {
            log.info("");
            log.info("========================================");
            log.info(String.format("%-20s %s", "SOCIAL_MEDIA", (currentUserId>0? ("Logged in as: "+currentUsername+" ("+currentUserId+")"):"[Not logged in]")));
            log.info("========================================");
            if (currentUserId <= 0) {
                log.info("1) Register            3) Request Password Reset");
                log.info("2) Login               4) Reset Password (token)");
                log.info("0) Exit");
                System.out.print("Choose an option: ");
                String opt = sc.nextLine();
                if (opt.equals("0")) break;
                try {
                    switch (opt) {
                        case "1" -> {
                            System.out.print("username: "); String u = sc.nextLine();
                            System.out.print("email: "); String e = sc.nextLine();
                            System.out.print("password: "); String p = sc.nextLine();
                            int id = auth.register(u, e, p);
                            log.info("Registered user id: {}", id);
                        }
                        case "2" -> {
                            System.out.print("username/email: "); String uname = sc.nextLine();
                            System.out.print("password: "); String pwd = sc.nextLine();
                            String token = auth.login(uname, pwd, "127.0.0.1", "cli");
                            log.info("Login success. token: {}", token);
                            currentToken = token;
                            Integer uid = sessionService.getUserId(token);
                            currentUserId = uid == null ? -1 : uid;
                            if (currentUserId > 0) {
                                org.example.model.user u = userService.findById(currentUserId);
                                currentUsername = u != null ? u.getUsername() : ("user-"+currentUserId);
                                log.info("Welcome, {}!", currentUsername);
                            }
                        }
                        case "3" -> {
                            System.out.print("enter username or email: "); String ue = sc.nextLine();
                            String resetToken = resetService.requestReset(ue);
                            log.info("Password reset token (demo): {}", resetToken);
                            log.info("Use option 4 with this token to set a new password.");
                        }
                        case "4" -> {
                            System.out.print("reset token: "); String t = sc.nextLine();
                            System.out.print("new password: "); String np = sc.nextLine();
                            resetService.resetWithToken(t, np);
                            log.info("Password updated. You can now login with the new password.");
                        }
                        default -> log.info("Invalid option");
                    }
                } catch (Exception ex) {
                    log.error("Error: {}", ex.getMessage(), ex);
                }
            } else {
                log.info("1) Global Feed         5) Like a Post");
                log.info("2) Following Feed      6) Comment on a Post");
                log.info("3) Create Post         9) Logout");
                log.info("4) Follow user         0) Exit");
                System.out.print("Choose an option: ");
                String opt = sc.nextLine();
                if (opt.equals("0")) break;
                try {
                    switch (opt) {
                        case "1" -> {
                            List<Document> posts = social.getFeed(10);
                            log.info("Feed:");
                            for (Document d : posts) {
                                ObjectId pid = d.getObjectId("_id");
                                long likeCount = social.countLikesForPost(pid);
                                long commentCount = social.countCommentsForPost(pid);
                                log.info("id={} userId={} user={} time={} likes={} comments={}\n  {}",
                                        pid, d.get("userId"), d.getString("username"), d.getDate("createdAt"), likeCount, commentCount, d.getString("content"));
                            }
                        }
                        case "2" -> {
                            List<Integer> followees = friendService.listFollowees(currentUserId);
                            if (!followees.contains(currentUserId)) followees.add(currentUserId); // include own posts
                            List<Document> posts = social.getFeedByAuthors(followees, 20);
                            log.info("Following Feed:");
                            for (Document d : posts) {
                                ObjectId pid = d.getObjectId("_id");
                                long likeCount = social.countLikesForPost(pid);
                                long commentCount = social.countCommentsForPost(pid);
                                log.info("id={} userId={} user={} time={} likes={} comments={}\n  {}",
                                        pid, d.get("userId"), d.getString("username"), d.getDate("createdAt"), likeCount, commentCount, d.getString("content"));
                            }
                        }
                        case "3" -> {
                            System.out.print("content: "); String content = sc.nextLine();
                            Document p = new Document()
                                    .append("userId", currentUserId)
                                    .append("username", currentUsername)
                                    .append("content", content)
                                    .append("createdAt", new Date());
                            social.createPost(p);
                            log.info("Post created.");
                        }
                        case "4" -> {
                            System.out.print("follow user id: "); int followee = Integer.parseInt(sc.nextLine());
                            boolean ok = friendService.follow(currentUserId, followee);
                            log.info(ok ? "Now following" : "Already following / error");
                        }
                        case "5" -> {
                            System.out.print("post id to like: "); String pid = sc.nextLine();
                            Document like = new Document()
                                    .append("targetType", "post")
                                    .append("targetId", new ObjectId(pid))
                                    .append("userId", currentUserId)
                                    .append("createdAt", new Date());
                            boolean ok = social.addLike(like);
                            log.info(ok ? "Liked." : "Already liked or error.");
                        }
                        case "6" -> {
                            System.out.print("post id to comment: "); String pid = sc.nextLine();
                            System.out.print("comment text: "); String text = sc.nextLine();
                            Document c = new Document()
                                    .append("postId", new ObjectId(pid))
                                    .append("userId", currentUserId)
                                    .append("text", text)
                                    .append("createdAt", new Date());
                            social.addComment(c);
                            log.info("Comment added.");
                        }
                        case "9" -> {
                            sessionService.invalidate(currentToken);
                            currentToken = null;
                            currentUserId = -1;
                            currentUsername = null;
                            log.info("Logged out.");
                        }
                        default -> log.info("Invalid option");
                    }
                } catch (Exception ex) {
                    log.error("Error: {}", ex.getMessage(), ex);
                }
            }
        }

        MongoProvider.close();
        log.info("bye");
    }

    private static void printBanner() {
        log.info("========================================");
        log.info("        WELCOME TO SOCIAL_MEDIA        ");
        log.info("========================================");
    }
}
