package ir.taha7900.yadnegar.Models;

import java.time.LocalDateTime;
import java.util.Date;

public class User {

    private static User currentUser;

    private long id;
    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private String email;
    private Date birthday_date;
    private String phone_number;
    private int[] friends;
    private LocalDateTime created;
    private String token;

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
