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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.MyReservation;

public class CancelActivity extends AppCompatActivity {

    ImageView imgCancel;
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
        imgCancel = findViewById(R.id.imgCancel);

        // 인텐트 받아오기
        MyReservation myReservation = (MyReservation) getIntent().getSerializableExtra("myReservation");

        // 스피너 설정
        String CancelPrice = String.valueOf(myReservation.getPrice());
        txtPrice.setText(CancelPrice + "원");

        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.cancel_reasons, android.R.layout.simple_spinner_item);
        spinner.setAdapter(yearAdapter);


        // X버튼  클릭 리스너
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 예약취소 버튼 클릭 리스너
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    public void popUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CancelActivity.this);
        builder.setMessage("예약을 취소하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //todo: 예약취소 api 호출
                        Toast.makeText(CancelActivity.this,"예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

