package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ReservationActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView txtHotelName;
    TextView txtReservation;
    TextView txtTime;
    TextView txtPrice;
    TextView txtUser;
    TextView txtPet;
    TextView txtPrice2;
    TextView txtCoupon;
    EditText editPoint;
    TextView txtTotalPoint;
    TextView txtFinalPrice;
    Button btnApproval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        imgBack = findViewById(R.id.imgBack);
        txtHotelName = findViewById(R.id.txtHotelName);
        txtReservation = findViewById(R.id.txtReservation);
        txtTime = findViewById(R.id.txtTime);
        txtPrice = findViewById(R.id.txtPrice);
        txtUser = findViewById(R.id.txtUser);
        txtPet = findViewById(R.id.txtPet);
        txtPrice2 = findViewById(R.id.txtPrice2);
        txtCoupon = findViewById(R.id.txtCoupon);
        editPoint = findViewById(R.id.editPoint);
        txtTotalPoint = findViewById(R.id.txtTotalPoint);
        txtFinalPrice = findViewById(R.id.txtFinalPrice);
        btnApproval = findViewById(R.id.btnApproval);

        //todo 뒤로가기(백버튼) 눌렀을때 처리(호텔 정보 액티비티 연결시키기)

        // 호텔 이름 셋팅
        txtHotelName.setText("");
        // 체크인 일정 셋팅 ex) 2023-02-28 (화) ~ 2023-03-01(수) | 1박
        txtReservation.setText("");
        // 체크인과 체크아웃 시간 셋팅 ex) 체크인 12:00 | 체크아웃 12:00
        txtTime.setText("");
        // 예약 금액 셋팅
        txtPrice.setText("");

        // 예약자 정보 셋팅 ex) 김이름 | 010-1234-5678
        txtUser.setText("");
        // 예약자 반려동물 셋팅 ex) 반려동물 | 장군이
        txtPet.setText("");

        // 예약 금액 셋팅
        txtPrice2.setText("");
        // 쿠폰 클릭시 새로운 액티비티 생성
        txtCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 포인트 사용시 입력받는 정보 가져오기
        String point = editPoint.getText().toString().trim();
        // 잔여 포인트 표시
        txtTotalPoint.setText("");

        // 최종 결제 금액 표시
        txtFinalPrice.setText("");




    }
}