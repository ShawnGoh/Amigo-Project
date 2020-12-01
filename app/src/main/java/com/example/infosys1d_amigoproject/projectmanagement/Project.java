package com.example.infosys1d_amigoproject.projectmanagement;

import android.net.Uri;

import com.example.infosys1d_amigoproject.models.users_display;

import java.util.ArrayList;

public class Project {


    private String projectitle, projectdescription,  thumbnail;
    private ArrayList<String> skillsrequired;
    private ArrayList<String> usersinProject;
    private String createdby;

    public Project() {

    }

    public Project(String thumbnail, String projectitle, String projectdescription, ArrayList<String> skillsrequired, ArrayList<String> usersinProject, String createdby) {
        this.thumbnail = thumbnail;
        this.projectitle = projectitle;
        this.projectdescription = projectdescription;
        this.skillsrequired = skillsrequired;
        this.usersinProject = usersinProject;
        this.createdby = createdby;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getProjectitle() {
        return projectitle;
    }

    public String getProjectdescription() {
        return projectdescription;
    }

    public ArrayList<String> getSkillsrequired() {
        return skillsrequired;
    }

    public ArrayList<String> getUsersinProject() {
        return usersinProject;
    }

    public String getCreatedby() {
        return createdby;
    }
}
