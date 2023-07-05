package com.vishcorp.vchat;

public class userprofile {
    public String uname,userID;

    public userprofile() {

    }

    public userprofile(String uname, String userID) {
        this.uname = uname;
        this.userID = userID;
    }

    public String getUname() { return uname; }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
