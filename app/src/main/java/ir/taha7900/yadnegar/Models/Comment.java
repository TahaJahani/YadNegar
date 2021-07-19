package ir.taha7900.yadnegar.Models;

public class Comment {
    private long id;
    private User memo_user;
    private String text;
    private Like[] likes;

    public long getId() {
        return id;
    }

    public User getMemoUser() {
        return memo_user;
    }

    public String getText() {
        return text;
    }

    public Like[] getLikes() {
        return likes;
    }
}
