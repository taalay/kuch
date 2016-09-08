package com.tali.admin.kuch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.tali.admin.kuch.R;

public class WelcomActivity extends AppCompatActivity {

    private DigitsAuthButton phoneButton;
    private AuthCallback authCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        setUpDigitsButton();
    }

    private AuthCallback initAuthCallback() {
        return new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String phoneNumber) {
                Toast.makeText(getApplicationContext(),
                        phoneNumber,
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WelcomActivity.this, RegisterActivity.class));
                finish();
            }

            @Override
            public void failure(DigitsException e) {
                // Answers.getInstance().logLogin(new LoginEvent().putMethod("Digits").putSuccess(false));
                Toast.makeText(getApplicationContext(),
                        e.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setUpDigitsButton() {
        authCallback = initAuthCallback();
        phoneButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        phoneButton.setAuthTheme(R.style.AppTheme);
        phoneButton.setCallback(authCallback);

    }
}
