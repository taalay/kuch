package com.tali.admin.kuch.activity;

import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tali.admin.kuch.R;
import com.tali.admin.kuch.data.db.DBHelper;
import com.tali.admin.kuch.model.PreferencesHelper;
import com.tali.admin.kuch.model.User;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_name_toolbar);
        final ImageView userImage = (ImageView) findViewById(R.id.a_user_image);
        TextView tName = (TextView) findViewById(R.id.a_user_name);
        TextView tLocation = (TextView) findViewById(R.id.a_user_location);
        final User user = DBHelper.getInstance(this).getUser(PreferencesHelper.getName());
        System.out.println(user.getUserName());
        if (user != null){
            if (user.getProfilePictureUrl() != null){
                Picasso.with(this).load(user.getProfilePictureUrl())
                        .placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .into(userImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                userImage.setImageURI(Uri.parse(user.getProfilePictureUrl()));
                            }
                        });
            }
            collapsingToolbar.setTitle(user.getUserName());
            tName.setText(user.getUserName());
            tLocation.setText(user.getLocation());
        }
    }
}