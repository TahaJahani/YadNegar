package ir.taha7900.yadnegar.Models;

import java.util.ArrayList;
import java.util.Date;

public class Tag {

    private static ArrayList<Tag> userTags;

    private long id;
    private long creator_user;
    private String name;
    private String color;

    public long getId() {
        return id;
    }

    public static ArrayList<Tag> getUserTags() {
        return userTags;
    }

    public static void addUserTag(Tag tag) {
        userTags.add(tag);
    }

    public static void setUserTags(ArrayList<Tag> userTags) {
        Tag.userTags = userTags;
    }

    public long getCreatorUser() {
        return creator_user;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreator_user(long creator_user) {
        this.creator_user = creator_user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
