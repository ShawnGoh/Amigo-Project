package com.example.infosys1d_amigoproject.models;

import android.net.Uri;

import com.example.infosys1d_amigoproject.R;

import java.util.ArrayList;

public class users_display {

    private String about_me = "Hi";
    private String bio = "Bio has not been written";
    private ArrayList<String> chats = new ArrayList<>();
    private String looking_for = "none";
    private String name;
    private String profile_picture = "none";
    private long projects_completed = 0;
    private ArrayList<String> projectTitle = new ArrayList<>();
    private ArrayList<String> projectDescription = new ArrayList<>();
    private ArrayList<String> skills = new ArrayList<>();
    private boolean compeletedsetup = false;
    private String status="offline";



    public users_display(String about_me, String bio, ArrayList<String> chats, String looking_for, String name, String profile_picture, long projects_completed, ArrayList<String> projectTitle, ArrayList<String> projectDescription, ArrayList<String> skills) {
        this.about_me = about_me;
        this.bio = bio;
        this.chats = chats;
        this.looking_for = looking_for;
        this.name = name;

        this.projects_completed = projects_completed;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.skills = skills;
        this.profile_picture = profile_picture;

    }

    public users_display() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCompeletedsetup() {
        return compeletedsetup;
    }

    public void setCompeletedsetup(boolean compeletedsetup) {
        this.compeletedsetup = compeletedsetup;
    }



    public void addchat(String s){this.chats.add(s);}

    public void removechat(String s){this.chats.remove(s);}


    public void addskills(String s){this.skills.add(s);}

    public void removeskills(String s){this.skills.remove(s);}

    public users_display(String name) {
        this.name = name;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ArrayList<String> getChats() {
        return chats;
    }

    public void setChats(ArrayList<String> chats) {
        this.chats = chats;
    }

    public String getLooking_for() {
        return looking_for;
    }

    public void setLooking_for(String looking_for) {
        this.looking_for = looking_for;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public long getProjects_completed() {
        return projects_completed;
    }

    public void setProjects_completed(long projects_completed) {
        this.projects_completed = projects_completed;
    }

    public ArrayList<String> getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(ArrayList<String> projectTitle) {
        this.projectTitle = projectTitle;
    }

    public ArrayList<String> getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(ArrayList<String> projectDescription) {
        this.projectDescription = projectDescription;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }
}
