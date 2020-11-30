package com.example.infosys1d_amigoproject.models;

public class Userdataretrieval {

    private users_display usersdisplay;
    private users_private usersprivate;


    public Userdataretrieval(users_display usersdisplay, users_private usersprivate) {
        this.usersdisplay = usersdisplay;
        this.usersprivate = usersprivate;
    }

    public Userdataretrieval() {
    }

    @Override
    public String toString() {
        return "Userdataretrieval{" +
                "usersdisplay=" + usersdisplay +
                ", usersprivate=" + usersprivate +
                '}';
    }

    public users_display getUsersdisplay() {
        return usersdisplay;
    }

    public void setUsersdisplay(users_display usersdisplay) {
        this.usersdisplay = usersdisplay;
    }

    public users_private getUsersprivate() {
        return usersprivate;
    }

    public void setUsersprivate(users_private usersprivate) {
        this.usersprivate = usersprivate;
    }
}
