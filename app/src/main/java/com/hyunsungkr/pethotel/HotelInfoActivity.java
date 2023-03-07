package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class HotelInfoActivity extends AppCompatActivity {
    RatingBar ratingBar;
    ImageView imgHotel;
    ImageView imgFavorite;
    TextView txtHotelName;
    TextView txtReviewAvg;
    TextView txtReviewSum;
    TextView txtSmall;
    TextView txtSmallPrice;
    TextView txtDescription;
    TextView txtMedium;
    TextView txtMediumPrice;
    TextView txtLarge;
    TextView txtLargePrice;
    Button btnLarge;
    Button btnSmall;
    Button btnMedium;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

        ratingBar = findViewById(R.id.ratingBar);
        imgHotel = findViewById(R.id.imgHotel);
        imgFavorite = findViewById(R.id.imgFavorite);
        txtHotelName = findViewById(R.id.txtHotelName);
        txtReviewAvg = findViewById(R.id.txtReviewAvg);
        txtReviewSum = findViewById(R.id.txtReviewSum);
        txtSmall = findViewById(R.id.txtSmall);
        txtSmallPrice = findViewById(R.id.txtSmallPrice);
        txtMedium = findViewById(R.id.txtMedium);
        txtMediumPrice = findViewById(R.id.txtMediumPrice);
        txtLarge = findViewById(R.id.txtLarge);
        txtLargePrice = findViewById(R.id.txtLargePrice);
        btnLarge = findViewById(R.id.btnLarge);
        btnSmall = findViewById(R.id.btnSmall);
        btnMedium = findViewById(R.id.btnMedium);
        txtDescription = findViewById(R.id.txtDescription);

    }
}