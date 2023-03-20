package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

public class ReviewWriteActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgPhoto;
    EditText editReview;
    RatingBar ratingBar;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        imgBack = findViewById(R.id.imgBack);
        imgPhoto = findViewById(R.id.imgPhoto);
        editReview = findViewById(R.id.editReview);
        ratingBar = findViewById(R.id.ratingBar);
        btnSave = findViewById(R.id.btnSave);





    }
}