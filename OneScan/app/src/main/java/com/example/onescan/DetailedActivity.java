package com.example.onescan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class DetailedActivity extends AppCompatActivity {

    private final static int GOT_SELECTED_IMAGE = 666;
    private final static int GOT_IMAGE_FROM_CAMERA = 999;


    ImageView imageView;
    // get the Global bitmap field
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    Bitmap imageBitmap;
    String[] information;
//    https://www.flaticon.com/free-icon/device_2905997?related_id=2905997

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        imageView = findViewById(R.id.imageView_backgroud);
        textView1 = findViewById(R.id.textviewDeviceName);
        textView2 = findViewById(R.id.textviewDeviceSop);
        textView3 = findViewById(R.id.textviewDeviceCali);
        textView4 = findViewById(R.id.textviewDeviceRecord);

        Intent intent= getIntent();
        int requestCode = intent.getExtras().getInt("requestCode");
//        String rq = String.valueOf(requestCode);
        String rq = "Picture Captured";
        Toast.makeText(this, rq, Toast.LENGTH_SHORT).show();
        if (requestCode==GOT_SELECTED_IMAGE){
            Uri selectedImage = intent.getParcelableExtra("imageUri");
            Bitmap imageBitmap = null;
            try {
                imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(imageBitmap);

        }

        if (requestCode==GOT_IMAGE_FROM_CAMERA){
            imageBitmap = new GlobalVariables().getMyBitmap();
        }

//        imageBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.babyshark1);
        information = new String[]{"BabyShark", "@string/pinkfongstr", "July-20-2021", "https://www.youtube.com/Pinkfong/featured"};
        // Load the Captured Image
        loadCapturedImage(imageBitmap);
        loadInformation(information);

    }

    private void loadCapturedImage(Bitmap imageBitmap) {
        if (imageBitmap!=null){
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void loadInformation(String[] information){
        textView1.setText(information[0]);
        textView2.setText(information[1]);
        textView2.setText(R.string.pinkfongstr);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView3.setText(information[2]);
        textView4.setText(information[3]);
        textView4.setText(R.string.pinkfongwiki);
        textView4.setMovementMethod(LinkMovementMethod.getInstance());
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