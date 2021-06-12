package com.example.onescan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoAlbum(View v){
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }

    public void gotoInstuction(View v){
        Intent intent = new Intent(this, InstructionActivity.class);
        startActivity(intent);
    }

    public void gotoPicture(View v){
        Intent intent = new Intent(this, PictureActivity.class);
        startActivity(intent);
    }
}