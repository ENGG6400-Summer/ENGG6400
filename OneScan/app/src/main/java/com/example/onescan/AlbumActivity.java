package com.example.onescan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;

public class AlbumActivity extends AppCompatActivity {
    private final static int GOT_SELECTED_IMAGE = 666;
    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        imageView = findViewById(R.id.imageView_album);
        button = findViewById(R.id.button_album);

        Intent intent= getIntent();
        int requestCode = intent.getExtras().getInt("requestCode");
        Uri selectedImage = intent.getParcelableExtra("imageUri");
        if (requestCode==GOT_SELECTED_IMAGE){
                    Bitmap imageBitmap = null;
            try {
                imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        imageView.setImageBitmap(imageBitmap);

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GOT_SELECTED_IMAGE){
            Uri selectedImage = data.getParcelableExtra("imageUri");
            Bitmap imageBitmap = null;
            try {
                imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(imageBitmap);
       }
    }
}