package ir.taha7900.yadnegar.Models;

import java.util.Date;

public class Tag {
    private long id;
    private long creator_user;
    private String name;
    private String color;

    public long getId() {
        return id;
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
}
