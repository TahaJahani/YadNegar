package ir.taha7900.yadnegar.Models;

public class Like {
    private long id;
    private User memo_user;

    public long getId() {
        return id;
    }

    public User getMemoUser() {
        return memo_user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMemo_user(User memo_user) {
        this.memo_user = memo_user;
    }
}
