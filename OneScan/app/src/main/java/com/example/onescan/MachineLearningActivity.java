package com.example.onescan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.FloatArrayEvaluator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onescan.ml.MobilenetV110224Quant;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.schema.Buffer;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class MachineLearningActivity extends AppCompatActivity {
    ImageView imageView;
    Button button_take_picture;
    Button button_make_predict;
    TextView textView_result;
    int SELECT_FROM_ALBUM_FOR_ML = 100;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_learning);
        button_take_picture = findViewById(R.id.takepicforlearning);
        button_make_predict = findViewById(R.id.makeprediction);
        textView_result = findViewById(R.id.predictresult);
        imageView = findViewById(R.id.imageViewforMachineLearning);
        Context context = this;



        // important the label text file:
        String filename = "labels.txt";
        ArrayList<String> alllabels = new ArrayList<>();
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.labels));
        while (scan.hasNextLine()){
            String line = scan.nextLine();
            alllabels.add(line);
        }
        scan.close();




        button_take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, SELECT_FROM_ALBUM_FOR_ML);
            }
        });

        button_make_predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap resized_bitmap = Bitmap.createScaledBitmap(bitmap, 224,224,true);

                try {
                    MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(context);

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
                    textView_result.setText(alllabels.get(max));

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Dispaly the image via uri
        imageView.setImageURI(data.getData());

        // Convert the uri to bitmap
        Uri uri = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        button_make_predict.callOnClick();
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
}