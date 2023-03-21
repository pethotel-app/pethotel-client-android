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

import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.ReservationApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.CancelHotel;
import com.hyunsungkr.pethotel.model.CancelReason;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.MyReservation;
import com.hyunsungkr.pethotel.model.MyReservationList;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CancelActivity extends AppCompatActivity {

    ImageView imgCancel;
    Spinner spinner;
    TextView txtPrice;
    Button btnCancel;
    String accessToken;
    MyReservation myReservation;
    String CancelReason;
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
        myReservation = (MyReservation) getIntent().getSerializableExtra("myReservation");

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
                if (spinner == null || spinner.getSelectedItem().toString().equals("취소 사유를 선택해주세요.")){
                    Toast.makeText(CancelActivity.this, "취소사유를 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    CancelReason = spinner.getSelectedItem().toString();
                popUp();}

            }
        });


    }
    
    // 취소버튼 클릭이벤트 함수
    public void popUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CancelActivity.this);
        builder.setMessage("예약을 취소하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        putNetworkData();
                        getNetworkData();
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
    
    // 예약정보 취소 데이터통신
    public void getNetworkData(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(CancelActivity.this);
        ReservationApi api = retrofit.create(ReservationApi.class);


        Call<CancelHotel> call = api.deleteHotel(accessToken, myReservation.getHotelId(), myReservation.getPetId());

        call.enqueue(new Callback<CancelHotel>() {
            @Override
            public void onResponse(Call<CancelHotel> call, Response<CancelHotel> response) {
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<CancelHotel> call, Throwable t) {
                Toast.makeText(CancelActivity.this, "예약취소 실패!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
    public void putNetworkData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(CancelActivity.this);
        ReservationApi api = retrofit.create(ReservationApi.class);

        // 요청 바디 생성
        JSONObject json = new JSONObject();
        try {
            json.put("hotelId", myReservation.getHotelId());
            json.put("reason",CancelReason);
            json.put("cancelPrice",myReservation.getPrice());
            json.put("resCreatedAt", myReservation.getCreatedAt());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString());

        Call<com.hyunsungkr.pethotel.model.CancelReason> call = api.reasonCancel(accessToken,requestBody);

        call.enqueue(new Callback<com.hyunsungkr.pethotel.model.CancelReason>() {
            @Override
            public void onResponse(Call<com.hyunsungkr.pethotel.model.CancelReason> call, Response<com.hyunsungkr.pethotel.model.CancelReason> response) {
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<com.hyunsungkr.pethotel.model.CancelReason> call, Throwable t) {

            }
        });
    }
}

