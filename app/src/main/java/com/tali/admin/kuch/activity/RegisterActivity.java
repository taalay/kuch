package com.tali.admin.kuch.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.tali.admin.kuch.R;
import com.tali.admin.kuch.data.db.DBHelper;
import com.tali.admin.kuch.model.PreferencesHelper;
import com.tali.admin.kuch.util.Util;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int TAKEPICTURE = 1;
    private static final int FROMGALLERY = 2;
    private static final String PROFILE_PHOTO_NAME = "profile";
    private ImageView userImage;
    private Bitmap userImageBitmap;
    private String photoPath;
    private AutoCompleteTextView aLocation;
    private int photoisFromGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userImage = (ImageView) findViewById(R.id.l_user_image);
        aLocation = (AutoCompleteTextView) findViewById(R.id.l_location);

        aLocation.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.own_list_item));
        userImage.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.loggin) {
            if (tryToLoggin()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean tryToLoggin() {
        EditText eUserName = (EditText) findViewById(R.id.l_name);
        RadioGroup rSexGroup = (RadioGroup) findViewById(R.id.redio_group_sex);

        String userName = eUserName.getText().toString();
        String userLocation = aLocation.getText().toString();
        String sex = getSex(rSexGroup);
        String photoPath = getPhotoPath();
        String phoneNumber = Digits.getActiveSession() != null ? Digits.getActiveSession()
                .getPhoneNumber() : "";
        boolean result = true;
        if (userName.length() == 0) {
            eUserName.setError("Введите имя");
            result = false;
        }
        if (userLocation.length() == 0) {
            aLocation.setError("Введите город, населенный пунк");
            result = false;
        }
        if (result) {
            PreferencesHelper.setUserInformation(userName, phoneNumber, userLocation, sex, photoPath);
            System.out.println("hui" + PreferencesHelper.getUser().toString());
            DBHelper.getInstance(this).addOrUpdateUser(PreferencesHelper.getUser());
        }
        return result;
    }

    private String getSex(RadioGroup rSexGroup) {
        int id = rSexGroup.getCheckedRadioButtonId();
        if (id == R.id.male) {
            return getString(R.string.male);
        } else {
            return getString(R.string.female);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l_user_image:
                AlertDialog dialog = getDialogFragment();
                dialog.show();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKEPICTURE:
                if (resultCode == RESULT_OK) {
                    Bitmap mphoto = (Bitmap) data.getExtras().get("data");
                    userImage.setImageBitmap(mphoto);
                    userImageBitmap = mphoto;
                    photoisFromGallery = TAKEPICTURE;
                }

                break;
            case FROMGALLERY:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    userImage.setImageURI(selectedImage);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    photoPath = cursor.getString(columnIndex);
                    photoisFromGallery = FROMGALLERY;
                }
                break;
        }

    }


    public AlertDialog getDialogFragment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Фотография");
        builder.setPositiveButton("Загрузить из галереи", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getImageFromGallery();
            }
        });
        builder.setNegativeButton("Сделать снимок", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                takePicture();
            }
        });


        return builder.create();
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

    private String getPhotoPath() {
        if (photoisFromGallery == FROMGALLERY) {
            return photoPath;
        } else if (photoisFromGallery == TAKEPICTURE) {
            if (userImageBitmap != null) {
                try {
                    return Util.savebitmap(userImageBitmap, PROFILE_PHOTO_NAME);
                } catch (IOException e) {
                    Toast.makeText(this, "RegisterActivity не удалось созранить файл",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return "";
                }
            }
        }
        return "";
    }
}
