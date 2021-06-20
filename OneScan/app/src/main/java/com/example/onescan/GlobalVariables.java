package com.example.onescan;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GlobalVariables extends Application {
    private String subject;
    private Boolean databaseConnected = false;
    private static Bitmap myBitmap;
    private FirebaseAuth auth;
    private String DeviceName;

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

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

    public Boolean getDatabaseConnected() {
        return databaseConnected;
    }

    public void setDatabaseConnected(Boolean databaseConnected) {
        this.databaseConnected = databaseConnected;
    }
//
//    public Boolean getRTDatabaseConnected() {
//        String username = "onescan@gmail.com";
//        String password = "password";
//        Task t = auth.signInWithEmailAndPassword(username,password);
//        t.addOnCompleteListener(this, new  OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    setDatabaseConnected(true);
//                }else{
//                    setDatabaseConnected(false);
//                }
//
//            }
//        });
//    }
}