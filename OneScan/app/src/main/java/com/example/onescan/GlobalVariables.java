package com.example.onescan;

import android.app.Application;
import android.graphics.Bitmap;

public class GlobalVariables extends Application {
    private String subject;
    private static Bitmap myBitmap;

    public Bitmap getMyBitmap() {
        return myBitmap;
    }

    public void setMyBitmap(Bitmap myBitmap) {
        this.myBitmap = myBitmap;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}