package ir.taha7900.yadnegar.Models;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

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
    private String created;
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

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName(){
        return first_name + " " + last_name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday_date() {
        return birthday_date;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int[] getFriends() {
        return friends;
    }

    public String getCreated() {
        return created;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return ((User) o).id == this.id;
    }
}
