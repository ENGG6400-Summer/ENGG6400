package com.example.onescan;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean dataBaseConected;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String DeviceName;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBaseConected = ((GlobalVariables) getActivity().getApplication()).getDatabaseConnected();
        if (dataBaseConected==true){
            View v = inflater.inflate(R.layout.fragment_detail, container, false);
            return v;
        }else{
            View v = inflater.inflate(R.layout.fragment_nodb_detail, container, false);
            return v;
        }

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (dataBaseConected==true){
        TextView textView1 = getView().findViewById(R.id.textviewDeviceName);
        TextView textView2 = getView().findViewById(R.id.textviewDeviceSop);
        TextView textView3 = getView().findViewById(R.id.textviewDeviceCali);
        TextView textView4 = getView().findViewById(R.id.textviewDeviceRecord);
        DeviceName =  ((GlobalVariables) getActivity().getApplication()).getDeviceName();


        // Read Data From Database:
        HashMap<String, String> map = new HashMap<>();
        DatabaseReference workdb = FirebaseDatabase.getInstance().getReference().child(DeviceName);
        workdb.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                map.clear();
                Iterable<DataSnapshot> snap = snapshot.getChildren();
                for (DataSnapshot s: snap){
                    String k = s.getKey();
                    String v = s.getValue().toString();
                    map.put(k,v);
                }
                //        Set the content to the TextView
                String myname = (String) map.get("name");
                String sop = (String) map.get("sop");
                String mycalibration = (String) map.get("calibration");
                String result = (String) map.get("result");

                textView1.setText(myname);
                textView2.setText(sop);
                textView2.setTextColor(Color.parseColor("#0d18e0"));
                textView3.setText(mycalibration);
                textView4.setText(result);
                textView4.setTextColor(Color.parseColor("#0d18e0"));

                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DeviceName.equals("None")){
                            Intent intent = new Intent(getActivity(),InstructionActivity.class);
                            startActivity(intent);
                        }else{
                        Uri webpage = Uri.parse(sop);
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        startActivity(webIntent);}
                    }
                });

                textView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DeviceName.equals("None")){
                            Intent intent = new Intent(getActivity(),InstructionActivity.class);
                            startActivity(intent);
                        }else {
                            Uri webpage = Uri.parse(result);
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            startActivity(webIntent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }});
    }else{
            TextView textView1 = getView().findViewById(R.id.textviewDeviceName);
            DeviceName =  ((GlobalVariables) getActivity().getApplication()).getDeviceName();
            textView1.setText(DeviceName);
        }
}}