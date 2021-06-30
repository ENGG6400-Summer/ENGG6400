package com.example.onescan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    Button button_album;
    ImageView imageView_main;
    private final static int PICK_PHOTO = 250;
    private final static int GOT_SELECTED_IMAGE = 666;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check connection to firebase database
        auth = FirebaseAuth.getInstance();
        String username = "onescan@gmail.com";
        String password = "password";
        connectDatabase(username, password);

        // check permission for camera
        if(!checkPermissionForCamera())
            requestPermissionForCamera();
        imageView_main = findViewById(R.id.imageView_Main);

        button_album = findViewById(R.id.button_album_in_main);
//        Set the onclick listener for the album button
        button_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto,PICK_PHOTO);
            }
        });
    }

    public void gotoInstuction(View v){
        Intent intent = new Intent(this, InstructionActivity.class);
        startActivity(intent);
    }
    public void sampleresult(View v){
        Intent intent = new Intent(this, DetailedActivity.class);
        startActivity(intent);
    }

    public void gotoPicture(View v){
        Intent intent = new Intent(this, PictureActivity.class);
        startActivity(intent);
    }

    public void gotoMachineLearning(View v){
        Intent intent = new Intent(this, MachineLearningActivity.class);
        startActivity(intent);
    }
    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            Toast.makeText(this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_PHOTO && resultCode==RESULT_OK){
            // The returned URl is not a real URL, it is a input Stream
            Uri selectedImage = data.getData();
            Intent intent = new Intent(this, DetailedActivity.class);
            intent.putExtra("imageUri", selectedImage);
            intent.putExtra("requestCode", GOT_SELECTED_IMAGE);
            startActivity(intent);
//            startActivityForResult(intent,GOT_SELECTED_IMAGE);
//            Bitmap imageBitmap = null;
//            try {
//                imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            imageView_main.setImageBitmap(imageBitmap);
        }
    }

    // get a file from a URI
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void connectDatabase(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new  OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    ((GlobalVariables) MainActivity.this.getApplication()).setDatabaseConnected(true);
                    Toast.makeText(MainActivity.this, "Database Connection Successful",Toast.LENGTH_SHORT).show();
                }else{
                    ((GlobalVariables) MainActivity.this.getApplication()).setDatabaseConnected(false);
                    Toast.makeText(MainActivity.this, "Database Connection Failed,\nCan only display machine name",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}