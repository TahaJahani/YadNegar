package ir.taha7900.yadnegar.Models;

import java.util.Date;

public class User {

    private static User currentUser;

    private long id;
    private String name;
    private String username;
    private Date birthDate;
    private String phoneNumber;


    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logoutCurrentUser() {
        User.currentUser = null;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }
}
