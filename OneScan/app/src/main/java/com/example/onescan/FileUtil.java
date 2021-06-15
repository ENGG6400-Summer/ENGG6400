package com.example.onescan;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    private static final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    //保存照片
    public static void saveBitmap(Bitmap b) {
        String jpegName = rootPath + "/" +   "test.jpg";
        File file= new File(jpegName);
        if(file.exists()) file.delete();
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

