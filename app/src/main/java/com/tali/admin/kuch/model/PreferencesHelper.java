package com.tali.admin.kuch.model;

import android.content.SharedPreferences;

import com.tali.admin.kuch.App;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 23.08.2016.
 */
public class PreferencesHelper {

    private static final String NAME = "name";
    private static final String MIDDLENAME = "middlename";
    private static final String REGION = "region";
    private static final String PHOTOPATH = "photoUrl";
    private static final String DIGITSID = "digitsId";
    private static final String NUMBER = "phoneNumber";
    private static final String SEX = "sex";
    private static SharedPreferences preferences = App.getUserPreferences();

    public static String getUserInformation() {
        return "PreferencesHelper{" +
                "name='" + getName() + '\'' +
                ", middleName='" + getMiddleName() + '\'' +
                ", region='" + getRegion() + '\'' +
                ", photoPath='" + getPhotoPath() + '\'' +
                ", number='" + getNumber() + '\'' +
                ", sex='" + getSex() + '\'' +
                ", digitsId=" + getDigitsId() +
                '}';
    }

    public static void setUserInformation(String name, String phoneNumber, String location, String sex, String photoPath) {
        setName(name);
        setPhotoPath(photoPath);
        setNumber(phoneNumber);
        setRegion(location);
        setSex(sex);

        // pullToServer();
    }

    public static String getName() {
        if (preferences.contains(NAME)) {
            return preferences.getString(NAME, "");
        }
        return "";
    }

    public static void setName(String name) {
        getEditor().putString(NAME, name).commit();
    }

    public static String getMiddleName() {
        if (preferences.contains(MIDDLENAME)) {
            return preferences.getString(MIDDLENAME, "");
        }
        return "";
    }

    public static void setMiddleName(String middleName) {
        getEditor().putString(MIDDLENAME, middleName).commit();
    }

    public static String getRegion() {
        if (preferences.contains(REGION)) {
            return preferences.getString(REGION, "");
        }
        return "";
    }

    public static void setRegion(String region) {
        getEditor().putString(REGION, region).commit();
    }

    public static String getPhotoPath() {
        if (preferences.contains(PHOTOPATH)) {
            return preferences.getString(PHOTOPATH, "");
        }
        return "";
    }

    public static void setPhotoPath(String photoPath) {
        getEditor().putString(PHOTOPATH, photoPath).commit();
    }

    public static long getDigitsId() {
        if (preferences.contains(DIGITSID)) {
            return preferences.getLong(DIGITSID, 0);
        }
        return 0;
    }

    public static void setDigitsId(long digitsId) {
        getEditor().putLong(DIGITSID, digitsId).commit();
    }

    public static String getNumber() {
        if (preferences.contains(NUMBER)) {
            return preferences.getString(NUMBER, "");
        }
        return "";
    }

    public static User getUser() {
        User temp = new User();
        temp.setProfilePictureUrl(getPhotoPath());
        temp.setUserName(getName());
        temp.setPhoneNumber(getNumber());
        temp.setLocation(getRegion());
        return temp;
    }

    public static void setNumber(String number) {
        getEditor().putString(NUMBER, number).commit();
    }

    private static SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    public static void clearUserPreferences() {
        getEditor().clear().commit();
    }

    public static void pullToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add(NAME, getName())
                        .add(MIDDLENAME, getMiddleName())
                        .add(REGION, getRegion())
                        .add(PHOTOPATH, getPhotoPath())
                        .add(NUMBER, getNumber())
                        .add(DIGITSID, String.valueOf(getDigitsId()))
                        .build();
                Request request = new Request.Builder()
                        .url("http://176.126.167.231:86/import/")
                        .post(formBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    System.out.println(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String getSex() {
        if (preferences.contains(SEX)) {
            return preferences.getString(SEX, "");
        }
        return "";
    }

    public static void setSex(String sex) {
        getEditor().putString(SEX, sex).commit();
    }
}
