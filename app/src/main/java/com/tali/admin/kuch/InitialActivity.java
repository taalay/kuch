package com.tali.admin.kuch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.digits.sdk.android.Digits;
import com.tali.admin.kuch.activity.MainActivity;
import com.tali.admin.kuch.activity.RegisterActivity;
import com.tali.admin.kuch.activity.WelcomActivity;
import com.tali.admin.kuch.model.PreferencesHelper;

public class InitialActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Digits.getActiveSession() == null) {
            if (PreferencesHelper.getName().equals("")){
                startActivityRegister();
            }else {
                startThemeActivity();
            }
        } else {
            startLoginActivity();
        }
    }

    private void startActivityRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void startThemeActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, WelcomActivity.class));
    }
}
