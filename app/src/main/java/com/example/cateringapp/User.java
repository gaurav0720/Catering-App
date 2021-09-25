package com.example.cateringapp;

public class User {

    private String fName, cName, IDNumber, mobNumber, emailID, password, userID,profilePhoto;

    public User(String fName, String cName, String IDNumber, String mobNumber, String emailID,
                String password, String userID, String profilePhoto) {
        this.fName = fName;
        this.cName = cName;
        this.IDNumber = IDNumber;
        this.mobNumber = mobNumber;
        this.emailID = emailID;
        this.password = password;
        this.userID = userID;
        this.profilePhoto = profilePhoto;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public User() {
    }
}
