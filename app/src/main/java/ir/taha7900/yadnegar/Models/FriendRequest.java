package ir.taha7900.yadnegar.Models;

import java.util.ArrayList;

public class FriendRequest {

    public static final String STATUS_ACCEPTED = "accepted";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_REJECTED = "rejected";

    private static ArrayList<FriendRequest> userFriendRequests;

    private long id;
    private User from_user;
    private long to_user;
    private String status;
    private String created;
    private String modified;

    public static ArrayList<FriendRequest> getUserFriendRequests() {
        return userFriendRequests;
    }

    public static void setUserFriendRequests(ArrayList<FriendRequest> userFriendRequests) {
        FriendRequest.userFriendRequests = userFriendRequests;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getFromUser() {
        return from_user;
    }

    public void setFromUser(User from_user) {
        this.from_user = from_user;
    }

    public long getToUser() {
        return to_user;
    }

    public void setToUser(long to_user_id) {
        this.to_user = to_user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getFormattedCreationDate() {
        String date = created.split("T")[0];
        String time = created.split("T")[1].split("\\.")[0];
        return date + ", " + time;
    }
}
