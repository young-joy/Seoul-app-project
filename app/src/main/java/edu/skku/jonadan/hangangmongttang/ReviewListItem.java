package edu.skku.jonadan.hangangmongttang;

public class ReviewListItem {
    private int rid;
    private String user;
    private String password;
    private String date;
    private float rate;
    private String content;

    public ReviewListItem(int rid, String user, String password, String date, float rate, String content) {
        this.rid = rid;
        this.user = user;
        this.password = password;
        this.date = date;
        this.rate = rate;
        this.content = content;
    }

    public int getRid() {
        return rid;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDate() {
        return date;
    }

    public float getRate() {
        return rate;
    }

    public String getContent() {
        return content;
    }
}
