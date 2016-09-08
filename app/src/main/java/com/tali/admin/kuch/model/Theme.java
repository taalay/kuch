package com.tali.admin.kuch.model;

import java.util.ArrayList;

/**
 * Created by admin on 30.08.2016.
 */
public class Theme {
    private String description, location;
    private ArrayList<String> pictures;
    private String authorImg;
    private String authorName;
    private String themeDate;
    private String themeImg;

    public Theme(String authorName, String themeDate,String description,String themeImg, String location) {
        this.authorName = authorName;
        this.themeDate = themeDate;
        this.description = description;
        this.themeImg = themeImg;
        this.location = location;
    }

    public ArrayList<String> getPictures() {
        return pictures == null ? new ArrayList<String>() : pictures;
    }
    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", pictures=" + pictures +
                ", authorImg='" + authorImg + '\'' +
                ", authorName='" + authorName + '\'' +
                ", themeDate='" + themeDate + '\'' +
                ", themeImg='" + themeImg + '\'' +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setAuthorImg(String authorImg) {
        this.authorImg = authorImg;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getAuthorImg() {
        return authorImg;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getThemeDate() {
        return themeDate;
    }

    public String getThemeImg() {
        return themeImg;
    }
}
