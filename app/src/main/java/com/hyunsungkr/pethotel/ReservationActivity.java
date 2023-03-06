package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ReservationActivity extends AppCompatActivity {

    TextView txtHotelName;
    TextView txtDate;
    TextView txtTime;
    TextView txtPrice;
    TextView txtUser;
    TextView txtPet;
    TextView txtPrice2;
    TextView txtTotalPoint;
    TextView txtFinalPrice;
    EditText editCoupon;
    EditText editPoint;
    Button btnPayment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        txtHotelName = findViewById(R.id.txtHotelName);
        txtDate = findViewById(R.id.txtReservation);
        txtTime = findViewById(R.id.txtTime);
        txtPrice = findViewById(R.id.txtPrice);
        txtPrice2 = findViewById(R.id.txtPrice2);
        txtUser = findViewById(R.id.txtUser);
        txtPet = findViewById(R.id.txtPet);
        txtTotalPoint = findViewById(R.id.txtTotalPoint);
        txtFinalPrice = findViewById(R.id.txtFinalPrice);
        editCoupon = findViewById(R.id.editCoupon);
        editPoint = findViewById(R.id.editPoint);
        btnPayment = findViewById(R.id.btnPayment);


    }
}