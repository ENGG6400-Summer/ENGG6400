package com.example.onescan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.onescan.ml.MobilenetV110224Quant;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DetailedActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final static int GOT_SELECTED_IMAGE = 666;
    private final static int GOT_IMAGE_FROM_CAMERA = 999;

    ImageView imageView;
    // get the Global bitmap field
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    Bitmap imageBitmap;
    String[] information;
    String DeviceName;
    private TextToSpeech mTextToSpeech;
    private FirebaseAuth auth;


//    https://www.flaticon.com/free-icon/device_2905997?related_id=2905997
//    Reference about how to read Screen
//    https://www.jianshu.com/p/da6af26b7483
//    Reference about how to do machine learning
//    https://teachablemachine.withgoogle.com/train/image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        imageView = findViewById(R.id.imageView_backgroud);
        textView1 = findViewById(R.id.textviewDeviceName);
        textView2 = findViewById(R.id.textviewDeviceSop);
        textView3 = findViewById(R.id.textviewDeviceCali);
        textView4 = findViewById(R.id.textviewDeviceRecord);
        textView5 = findViewById(R.id.detailedpageinformationheader);


        // prepare for read screen
        initTextToSpeech();

        Intent intent= getIntent();
        int requestCode = intent.getExtras().getInt("requestCode");

        // get the Bitmap imageBitmap from album
        if (requestCode==GOT_SELECTED_IMAGE){
            Uri selectedImage = intent.getParcelableExtra("imageUri");
            imageBitmap = null;
            try {
                imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(imageBitmap);
        }
        // get the Bitmap imageBitmap from camera
        if (requestCode==GOT_IMAGE_FROM_CAMERA){
            imageBitmap = new GlobalVariables().getMyBitmap();
        }

        information = new String[]{"BabyShark", "@string/pinkfongstr", "July-20-2021", "https://www.youtube.com/Pinkfong/featured"};
        // Load the Captured Image
        loadCapturedImage(imageBitmap);

        // important the labels text file:
        String filename = "labels.txt";
        ArrayList<String> alllabels = new ArrayList<>();
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.labels));
        while (scan.hasNextLine()){
            String line = scan.nextLine();
            alllabels.add(line);
        }
        scan.close();


        // Resize the image to 224x224
        Bitmap resized_bitmap = Bitmap.createScaledBitmap(imageBitmap, 224,224,true);
        try {
            MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

            // create byteBuffer from resized bitmap
            TensorImage tbuffer = TensorImage.fromBitmap(resized_bitmap);
            ByteBuffer byteBuffer = tbuffer.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] myarray = outputFeature0.getFloatArray();
            int max = getMax(myarray);
            DeviceName = alllabels.get(max);
//            Toast.makeText(this, alllabels.get(max), Toast.LENGTH_SHORT).show();
//            textView_result.setText(alllabels.get(max));

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

        // send data from activity to fragment (the the fragment know the name of the device
        ((GlobalVariables) DetailedActivity.this.getApplication()).setDeviceName(DeviceName);

        // Read out the name of the item it recongized!!! This is the end of this activity;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5 seconds
                mTextToSpeech.speak(DeviceName, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 1500);

//        loadInformation(information);
//        textView5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
//                    mTextToSpeech.speak(textView1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
//                }
//            }
//        });
    }

    private void loadCapturedImage(Bitmap imageBitmap) {
        if (imageBitmap!=null){
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void loadInformation(String[] information){
        textView1.setText(DeviceName);
        textView2.setText(information[1]);
        textView2.setText(R.string.pinkfongstr);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView3.setText(information[2]);
        textView4.setText(information[3]);
        textView4.setText(R.string.pinkfongwiki);
        textView4.setMovementMethod(LinkMovementMethod.getInstance());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5 seconds
                textView5.callOnClick();
            }
        }, 1500);

    }

    private int getMax(float[] arr){
        // return the index of the hightest value
        int ind = 0;
        float min = 0.0f;

        for (int i =0; i<arr.length;i++){
            if (arr[i]>min){
                ind = i;
                min = arr[i];
            }
        }
        return ind;
    }

    //try to read screen
    private void initTextToSpeech() {
        // Set Context,TextToSpeech.OnInitListener
        mTextToSpeech = new TextToSpeech(this, this);
        // Set Pitch Man -> Woman
        mTextToSpeech.setPitch(1.0f);
        // Set Speed
        mTextToSpeech.setSpeechRate(0.5f);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.CANADA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Didn't find any Data to Read", Toast.LENGTH_SHORT).show();
            }
        }
    }

}