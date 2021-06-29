package ir.taha7900.yadnegar.Models;

import java.io.Serializable;

public class Memory implements Serializable {
    private long id;
    private long userId;
    private String title;
    private String content;

    public Memory(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
