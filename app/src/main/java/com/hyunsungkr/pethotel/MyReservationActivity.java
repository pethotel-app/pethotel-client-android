package com.hyunsungkr.pethotel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hyunsungkr.pethotel.adapter.MyReservationAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.ReservationApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.MyReservation;
import com.hyunsungkr.pethotel.model.MyReservationList;
import com.hyunsungkr.pethotel.model.Res;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyReservationActivity extends AppCompatActivity {

    ImageView imgBack;
    RecyclerView recyclerView;
    MyReservationAdapter adapter;
    ArrayList<MyReservation> myReservationList = new ArrayList<>();


    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);

        imgBack = findViewById(R.id.imgBack);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyReservationActivity.this));


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
    }

    void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(MyReservationActivity.this);
        ReservationApi api = retrofit.create(ReservationApi.class);

        // 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;

        Call<MyReservationList> call = api.getMyReservation(accessToken,offset,limit);
        call.enqueue(new Callback<MyReservationList>() {
            @Override
            public void onResponse(Call<MyReservationList> call, Response<MyReservationList> response) {
                if(response.isSuccessful()){
                    myReservationList.clear();
                    count = response.body().getCount();
                    myReservationList.addAll(response.body().getItems());
                    offset = offset + count;

                    adapter = new MyReservationAdapter(MyReservationActivity.this,myReservationList);
                    recyclerView.setAdapter(adapter);
                }else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<MyReservationList> call, Throwable t) {

            }
        });
    }

}