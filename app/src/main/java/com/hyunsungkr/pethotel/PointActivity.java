package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class PointActivity extends AppCompatActivity {

    TextView txtPoint;
    Button btnSaveList;
    Button btnUseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        txtPoint = findViewById(R.id.txtPoint);
        btnSaveList = findViewById(R.id.btnSaveList);
        btnUseList = findViewById(R.id.btnUseList);




    }
}