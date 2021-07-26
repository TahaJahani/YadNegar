package ir.taha7900.yadnegar.Models;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Memory implements Serializable {

    private static ArrayList<Memory> userMemories = new ArrayList<>();

    private long id;
    private User creator_user;
    private String title;
    private String text;
    private ArrayList<User> tagged_people;
    private ArrayList<Tag> tags;
    private ArrayList<String> post_files;
    private ArrayList<Like> likes;
    private ArrayList<Comment> comments;
    private String created;
    private String modified;

    public long getId() {
        return id;
    }

    public User getCreatorUser() {
        return creator_user;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public ArrayList<User> getTaggedPeople() {
        return tagged_people;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public ArrayList<String> getPostFiles() {
        return post_files;
    }

    public ArrayList<Like> getLikes() {
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
        return this.post_files != null && this.post_files.size() > 0;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public void setPost_files(ArrayList<String> post_files) {
        this.post_files = post_files;
    }

    public void addPostFile(String postFile) {
        this.post_files.add(postFile);
    }

    public static ArrayList<Memory> getUserMemories() {
        return userMemories;
    }

    public static void setUserMemories(ArrayList<Memory> userMemories) {
        Memory.userMemories = userMemories;
    }

    public static void addUserMemory(Memory memory) {
        Memory.userMemories.add(memory);
    }

    public static void removeUserMemory(Memory memory) {
        Memory.userMemories.remove(memory);
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
    }

    public String getFormattedCreationDate() {
        String date = created.split("T")[0];
        String time = created.split("T")[1].split("\\.")[0];
        return date + ", " + time;
    }

    public void addLike(Like like) {
        this.likes.add(like);
    }

    public void setTaggedPeople(ArrayList<User> tagged_people) {
        this.tagged_people = tagged_people;
    }

    public String extractMemoryData() {
        Gson gson = new Gson();
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", this.getTitle());
        data.put("text", this.getText());
        ArrayList<Long> taggedPeople = new ArrayList<>();
        ArrayList<Long> tags = new ArrayList<>();
        for (User person : this.getTaggedPeople())
            taggedPeople.add(person.getId());
        for (Tag tag : this.getTags())
            tags.add(tag.getId());
        data.put("tagged_people", taggedPeople);
        data.put("tags", tags);
        System.out.println(gson.toJson(data));
        return gson.toJson(data);
    }
}
