package com.tali.admin.kuch.data;

import android.content.Context;

import com.tali.admin.kuch.data.db.DBHelper;
import com.tali.admin.kuch.model.Post;
import com.tali.admin.kuch.model.PreferencesHelper;
import com.tali.admin.kuch.model.User;

import java.util.List;

/**
 * Created by admin on 06.09.2016.
 */
public class ThemeData {
    public static List<Post> placeList(Context context) {
        User sampleUser = new User();
        sampleUser.setUserName("Steph");
        sampleUser.setProfilePictureUrl(PreferencesHelper.getPhotoPath());

        Post samplePost = new Post();
        samplePost.setUser(sampleUser);
        String text = "Виктор, хорошо, что у людей есть выбор. Конечно, люди среди сетевиков-админов обычно не глупые и сами смогут решить, что им нужно. Мы бы с удовольствием снизили цену, но это скажется на качестве обучения и удобстве для наших слушателей. Все-таки мы стараемся давать по-максимуму и люди это явно ценят. Приятно после тренингов получать восторженные отзывы от наших слушателей - значит, мы все правильно делаем.";
        samplePost.setDescription(text);
        samplePost.setLocation("Где то в жопе");
        samplePost.setThemeDate("02.10.2015");
        samplePost.setThemeImg("http://bm.img.com.ua/nxs/img/prikol/images/large/8/9/182598_397489.jpg");

        // Get singleton instance of database
        DBHelper databaseHelper = DBHelper.getInstance(context);

        // Add sample post to the database
        databaseHelper.addPost(samplePost);

        // Get all posts from database
       return databaseHelper.getAllPosts();
    }
}
