package ir.taha7900.yadnegar.Models;

import java.util.Date;

public class User {

    private static User currentUser;

    private long id;
    private String name;
    private String username;
    private Date birthDate;
    private String phoneNumber;

    public User(long id, String name, String username, Date birthDate, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logoutCurrentUser() {
        User.currentUser = null;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public long getId() {
        return id;
    }
}
