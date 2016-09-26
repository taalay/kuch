package com.tali.admin.kuch;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.digits.sdk.android.Digits;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by admin on 22.08.2016.
 */
public class App extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "byy3T9zwm5eknIKgZzfcag0jl";
    private static final String TWITTER_SECRET = "zKDY1Phzm3m6nqf4nvbbvlyetb5hjKn2H3qHDzv4biJhkvf1M3";


    private static App singleton;
    public static final String APP_PREFERENCES = "userPreferences";


    public static App getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Digits.Builder digitsBuilder = new Digits.Builder().withTheme(R.style.AppTheme);
        Fabric.with(this, new TwitterCore(authConfig), digitsBuilder.build(), new Digits.Builder().build());

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }

    public static SharedPreferences getUserPreferences() {
        return singleton.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

}
