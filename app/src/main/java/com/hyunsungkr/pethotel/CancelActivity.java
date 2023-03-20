package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.MyReservation;

public class CancelActivity extends AppCompatActivity {

    Spinner spinner;
    TextView txtPrice;
    Button btnCancel;
    String accessToken;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        txtPrice = findViewById(R.id.txtPrice);
        btnCancel = findViewById(R.id.btnCancel);
        spinner = findViewById(R.id.spinner);

        MyReservation myReservation = (MyReservation) getIntent().getSerializableExtra("myReservation");
        String CancelPrice = String.valueOf(myReservation.getPrice());
        txtPrice.setText(CancelPrice);

        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.cancel_reasons, android.R.layout.simple_spinner_item);
        spinner.setAdapter(yearAdapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}

