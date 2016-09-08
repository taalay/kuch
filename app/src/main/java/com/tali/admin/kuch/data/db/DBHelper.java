package com.tali.admin.kuch.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tali.admin.kuch.model.Post;
import com.tali.admin.kuch.model.Theme;
import com.tali.admin.kuch.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String USER_LOCATION = "userLocation";
    private static DBHelper sInstance;
    private static final String DESCRIPTION = "description";

    private static final String THEME_IMG = "themeImg";
    private static final String THEME_DATE = "themDate";
    private static final String LOCATION = "location";
    // Database Info
    private static final String DATABASE_NAME = "postsDatabase";

    private static final int DATABASE_VERSION = 1;
    // Table Names
    private static final String TABLE_POSTS = "posts";

    private static final String TABLE_USERS = "users";
    // Post Table Columns
    private static final String KEY_POST_ID = "id";

    private static final String KEY_POST_USER_ID_FK = "userId";
    // PreferencesHelper Table Columns
    private static final String KEY_USER_ID = "id";

    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PROFILE_PICTURE_URL = "profilePictureUrl";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(
                context,
                DATABASE_NAME,                               //Имя базы
                null,                                       //Курсор
                DATABASE_VERSION                                           //Версия базы
        );
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(null, "DB created");
        String CREATE_POSTS_TABLE = "create table if not exists "+TABLE_POSTS + "("
                        + KEY_POST_ID +" integer primary key,"   //Порядковый номер в базе
                        + KEY_POST_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," // Define a foreign key
                        + DESCRIPTION + " text,"                         //Описания
                        + THEME_IMG+ " text,"                            //изображение темы
                        + THEME_DATE+" text,"                       //дата
                        + LOCATION+" text"                        //локация
                        + ");";

        String CREATE_USERS_TABLE = "create table if not exists " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_PROFILE_PICTURE_URL + " TEXT" +
                PHONE_NUMBER + " TEXT" +
                USER_LOCATION + " TEXT" +
                ")";

        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);

        Log.d(null, "inserted");
    }

    public void addPost(Post post) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            long userId = addOrUpdateUser(post.getUser());

            ContentValues values = new ContentValues();
            values.put(KEY_POST_USER_ID_FK, userId);
            values.put(DESCRIPTION, post.getDescription());
            values.put(THEME_IMG, post.getThemeImg());
            values.put(THEME_DATE, post.getThemeDate());
            values.put(LOCATION, post.getLocation());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_POSTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public long addOrUpdateUser(User user) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_NAME, user.getUserName());
            values.put(KEY_USER_PROFILE_PICTURE_URL, user.getProfilePictureUrl());
            values.put(PHONE_NUMBER, user.getPhoneNumber());
            values.put(USER_LOCATION, user.getLocation());

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_NAME + "= ?", new String[]{user.getUserName()});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.getUserName())});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                        TABLE_POSTS,
                        TABLE_USERS,
                        TABLE_POSTS, KEY_POST_USER_ID_FK,
                        TABLE_USERS, KEY_USER_ID);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    User newUser = new User();
                    newUser.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                    newUser.setProfilePictureUrl(cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL)));
                    newUser.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
                    newUser.setLocation(cursor.getString(cursor.getColumnIndex(USER_LOCATION)));

                    Post newPost = new Post();
                    newPost.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                    newPost.setUser(newUser);
                    newPost.setThemeImg(cursor.getString(cursor.getColumnIndex(THEME_IMG)));
                    newPost.setThemeDate(cursor.getString(cursor.getColumnIndex(THEME_DATE)));
                    newPost.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
                    posts.add(newPost);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }

    public User getUser(String id){
        String USER_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'",
                        TABLE_USERS,
                        KEY_USER_NAME, id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_SELECT_QUERY, null);
        User newUser = null;
        try {
            if (cursor.moveToFirst()) {
                newUser = new User();
                newUser.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                newUser.setProfilePictureUrl(cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL)));
                newUser.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
                newUser.setLocation(cursor.getString(cursor.getColumnIndex(USER_LOCATION)));
            }
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return newUser;
    }

    public int updateUserProfilePicture(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_PROFILE_PICTURE_URL, user.getProfilePictureUrl());

        // Updating profile picture url for user with that userName
        return db.update(TABLE_USERS, values, KEY_USER_NAME + " = ?",
                new String[] { String.valueOf(user.getUserName()) });
    }

    public void deleteAllPostsAndUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_POSTS, null, null);
            db.delete(TABLE_USERS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}