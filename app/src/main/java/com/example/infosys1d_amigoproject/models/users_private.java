package com.example.infosys1d_amigoproject.models;

import androidx.annotation.NonNull;

public class users_private {
    private String email;
    private String firstname;
    private String lastname;
    private String user_id;


    public users_private(String email, String firstname, String lastname, String user_id) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.user_id = user_id;
    }

    public users_private() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
