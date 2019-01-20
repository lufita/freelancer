package com.dlmkotlin.freelancer;

public class Jobs {

    String name;
    String phone;
    String email;
    String jobsname;
    String jobsdetail;
    String taken;

    //Constructor

    public Jobs(String name, String phone, String email, String jobsname, String jobsdetail, String taken) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.jobsname = jobsname;
        this.jobsdetail = jobsdetail;
        this.taken = taken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobsname() {
        return jobsname;
    }

    public void setJobsname(String jobsname) {
        this.jobsname = jobsname;
    }

    public String getJobsdetail() {
        return jobsdetail;
    }

    public void setJobsdetail(String jobsdetail) {
        this.jobsdetail = jobsdetail;
    }

    public String getTaken() {
        return taken;
    }

    public void setTaken(String taken) {
        this.taken = taken;
    }
}
