package ir.taha7900.yadnegar.Models;

import java.util.ArrayList;

public class FriendRequest {

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

    public User getFrom_user() {
        return from_user;
    }

    public void setFrom_user(User from_user) {
        this.from_user = from_user;
    }

    public long getTo_user() {
        return to_user;
    }

    public void setTo_user(long to_user_id) {
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
}
