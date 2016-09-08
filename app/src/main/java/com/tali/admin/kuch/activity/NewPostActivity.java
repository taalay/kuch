package com.tali.admin.kuch.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tali.admin.kuch.R;
import com.tali.admin.kuch.data.db.DBHelper;
import com.tali.admin.kuch.model.Post;
import com.tali.admin.kuch.model.PreferencesHelper;
import com.tali.admin.kuch.model.User;
import com.tali.admin.kuch.util.Util;

import java.io.IOException;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int TAKEPICTURE = 1;
    private static final int FROMGALLERY = 2;
    private static final int REQUEST_PLACE_PICKER = 3;
    private static final int MIN_DESCRIPTION = 10;
    private int photoisFromGallery;

    private TextView description;
    private ImageView themeImgView;
    private TextView location;
    private Bitmap userImageBitmap;
    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        description = (EditText) findViewById(R.id.n_description);
        themeImgView = (ImageView) findViewById(R.id.n_theme_img);
        location = (TextView) findViewById(R.id.n_location);

        //img buttons
        ImageView imgLocation = (ImageView) findViewById(R.id.n_img_location);
        ImageView imgCamera = (ImageView) findViewById(R.id.n_img_camera);
        ImageView imgGallery = (ImageView) findViewById(R.id.n_img_gallery);
        imgCamera.setOnClickListener(this);
        imgGallery.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.n_img_location:
                onPickButtonClick();
                break;
            case R.id.n_img_camera:
                takePicture();
                break;
            case R.id.n_img_gallery:
                getImageFromGallery();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKEPICTURE:
                if (resultCode == RESULT_OK) {
                    Bitmap mphoto = (Bitmap) data.getExtras().get("data");
                    themeImgView.setVisibility(View.VISIBLE);
                    themeImgView.setImageBitmap(mphoto);
                    userImageBitmap = mphoto;
                    photoisFromGallery = TAKEPICTURE;
                }

                break;
            case FROMGALLERY:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    themeImgView.setVisibility(View.VISIBLE);
                    themeImgView.setImageURI(selectedImage);
                    photoisFromGallery = FROMGALLERY;

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    photoPath = cursor.getString(columnIndex);
                    cursor.close();
                }
                break;
            case REQUEST_PLACE_PICKER:
                if (resultCode == RESULT_OK){
                    // The user has selected a place. Extract the name and address.
                    final Place place = PlacePicker.getPlace(data, this);
                    final CharSequence address = place.getAddress();
                    location.setText(address);


                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.loggin) {
            if (tryToPost()){
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPickButtonClick() {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            LatLngBounds LatLng  = new LatLngBounds(new LatLng(40.968023,70.859871),
                    new LatLng(43.077392,78.552947));
            intentBuilder.setLatLngBounds(LatLng);
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    private boolean tryToPost() {

        String tempDescription = description.getText().toString();
        String tempLocation = location.getText().toString();

        boolean result = true;
        if (tempDescription.length() < MIN_DESCRIPTION) {
            description.setError("Введите описание, не менее " + MIN_DESCRIPTION + " символов");
            result = false;
        }
        if (tempLocation.length() <= 0) {
            Toast.makeText(this,"Укажите местонахождения", Toast.LENGTH_LONG).show();
            result = false;
        }
        if (result) {
            User user = PreferencesHelper.getUser();

            Post post = new Post();
            post.setDescription(tempDescription);
            post.setThemeImg(getPhotoPath());
            post.setLocation(tempLocation);
            post.setUser(user);
            DBHelper.getInstance(this).addPost(post);
        }
        return result;
    }

    public String getPhotoPath() {
        if (photoisFromGallery == FROMGALLERY) {
            return photoPath;
        } else if (photoisFromGallery == TAKEPICTURE) {
            if (userImageBitmap != null) {
                try {
                    return Util.savebitmap(userImageBitmap, String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {
                    Toast.makeText(this, "не удалось созранить файл",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return "";
                }
            }
        }
        return "";
    }

    private void takePicture() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, TAKEPICTURE);
    }

    private void getImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, FROMGALLERY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post, menu);
        return true;
    }
}
