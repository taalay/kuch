package com.tali.admin.kuch.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 03.09.2016.
 */
public class Util {
    public static String savebitmap(Bitmap bmp, String fileName) throws IOException {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/kuchFiles";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName + ".png");
        FileOutputStream fOut = new FileOutputStream(file);

        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();
        return file.getPath();
    }
}
