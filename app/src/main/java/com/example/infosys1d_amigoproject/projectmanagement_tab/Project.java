package com.example.infosys1d_amigoproject.projectmanagement_tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Project {
    private String thumbnail = "nil";
    private String projectitle = "nil", projectdescription = "nil";
    private List<String> skillsrequired = new ArrayList<String> (Arrays.asList("nil"));
    private List<String> usersinProject = new ArrayList<String> (Arrays.asList("nil"));
    private List<String> applicantsinProject = new ArrayList<String> (Arrays.asList(null));
    private String createdby, projectID;

    public Project() {

    }

    public Project(String thumbnail, String projectitle, String projectdescription, ArrayList<String> skillsrequired, ArrayList<String> usersinProject,String createdby, String projectID) {
        this.thumbnail = thumbnail;
        this.projectitle = projectitle;
        this.projectdescription = projectdescription;
        this.skillsrequired = skillsrequired;
        this.usersinProject = usersinProject;
        this.createdby = createdby;
        this.projectID = projectID;
    }

    public List<String> getApplicantsinProject() {return applicantsinProject;}

    public String getThumbnail() {
        return thumbnail;
    }

    public String getProjectitle() {
        return projectitle;
    }

    public String getProjectdescription() {
        return projectdescription;
    }

    public List<String> getSkillsrequired() {
        return skillsrequired;
    }

    public List<String> getUsersinProject() {
        return usersinProject;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getProjectID() {
        return projectID;
    }
}
