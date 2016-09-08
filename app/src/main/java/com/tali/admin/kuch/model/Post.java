package com.tali.admin.kuch.model;

import java.util.ArrayList;

/**
 * Created by admin on 06.09.2016.
 */
public class Post {
    private User user;
    private String description, location;

    private String themeDate;

    public void setUser(User user) {
        this.user = user;
    }

    public void setThemeDate(String themeDate) {
        this.themeDate = themeDate;
    }

    private String themeImg;

    public String getThemeDate() {
        return themeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getThemeImg() {
        return themeImg;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }

    public User getUser() {
        return user;
    }
}
