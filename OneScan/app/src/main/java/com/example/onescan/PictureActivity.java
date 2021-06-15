package com.example.onescan;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class PictureActivity extends AppCompatActivity {

    private CameraSurfaceView cameraSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        cameraSurfaceView = findViewById(R.id.csf);

    }

    public void takePic(View view)
    {

        cameraSurfaceView.capture();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSurfaceView.closeCamera();
    }


}






/*
public class PictureActivity extends AppCompatActivity {
//    https://www.youtube.com/watch?v=DPHkhamDoyc&t=198s
//    https://developer.android.com/training/camera/photobasics
//    startActivityForResult -> (with a image location name) -> Camera app
//    Camera App save to the image location
//    Then, read from there
    ImageView imageView;
    Button button;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    private File photoFile;
    private String FILE_NAME="photo.jpg";
    private Uri fileProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        imageView = findViewById(R.id.imageView_picture);
        button = findViewById(R.id.captureButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Capture Pressed", Toast.LENGTH_LONG).show();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    photoFile = getPhotoFile(FILE_NAME);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoFile);
                fileProvider =  FileProvider.getUriForFile(getApplicationContext(),"com.example.onescan.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }

    private File getPhotoFile(String file_name) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                file_name,
                ".jpg",
                storageDir
        );
        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Toast.makeText(getApplicationContext(), photoFile.toString(), Toast.LENGTH_LONG).show();
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imageView.setImageBitmap(imageBitmap);
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
*/

