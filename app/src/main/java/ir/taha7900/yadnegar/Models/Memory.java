package ir.taha7900.yadnegar.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Memory implements Serializable {
    private long id;
    private User creator_user;
    private String title;
    private String text;
    private User[] tagged_people;
    private Tag[] tags;
    private String[] post_files;
    private Like[] likes;
    private ArrayList<Comment> comments;
    private String created;
    private String modified;

    public long getId() {
        return id;
    }

    public User getCreator_user() {
        return creator_user;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public User[] getTagged_people() {
        return tagged_people;
    }

    public Tag[] getTags() {
        return tags;
    }

    public String[] getPost_files() {
        return post_files;
    }

    public Like[] getLikes() {
        return likes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }

    public boolean hasFiles() {
        return this.post_files != null && this.post_files.length > 0;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
