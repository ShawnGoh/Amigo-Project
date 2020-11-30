package com.example.infosys1d_amigoproject.projectmanagement;

import android.net.Uri;

import com.example.infosys1d_amigoproject.models.users_display;

import java.util.ArrayList;

public class Project {
    private Uri thumbnail;
    private String projectitle, projectdescription;
    private ArrayList<String> skillsrequired;
    private ArrayList<users_display> usersinProject;
    private users_display createdby;

    public Project(Uri thumbnail, String projectitle, String projectdescription, ArrayList<String> skillsrequired, ArrayList<users_display> usersinProject, users_display createdby) {
        this.thumbnail = thumbnail;
        this.projectitle = projectitle;
        this.projectdescription = projectdescription;
        this.skillsrequired = skillsrequired;
        this.usersinProject = usersinProject;
        this.createdby = createdby;
    }

    public Uri getThumbnail() {
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

    public ArrayList<users_display> getUsersinProject() {
        return usersinProject;
    }

    public users_display getCreatedby() {
        return createdby;
    }
}
