package ir.taha7900.yadnegar.Models;

import java.util.ArrayList;

public class Comment {
    private long id;
    private User memo_user;
    private long post;
    private String text;
    private ArrayList<Like> likes;
    private boolean isSending = false;

    public long getId() {
        return id;
    }

    public User getMemoUser() {
        return memo_user;
    }

    public String getText() {
        return text;
    }

    public ArrayList<Like> getLikes() {
        return likes;
    }

    public void addLike(Like like) {
        this.likes.add(like);
    }

    public boolean isSending() {
        return isSending;
    }

    public void setSending(boolean sending) {
        isSending = sending;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMemoUser(User memo_user) {
        this.memo_user = memo_user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLikes(ArrayList<Like> likes) {
        this.likes = likes;
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
    }
}
