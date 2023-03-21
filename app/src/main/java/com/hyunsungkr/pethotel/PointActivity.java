package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyunsungkr.pethotel.adapter.PointAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.PointApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Point;
import com.hyunsungkr.pethotel.model.PointList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PointActivity extends AppCompatActivity {

    TextView txtPoint;
    ImageView imgBack;

    TextView txtMore;
    String point;

    RecyclerView recyclerView;
    PointAdapter adapter;
    ArrayList<Point> pointList = new ArrayList<>();

    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);


        txtPoint = findViewById(R.id.txtPoint);

        txtMore = findViewById(R.id.txtMore);
        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PointActivity.this));

        Intent intent = getIntent();
        point = intent.getStringExtra("point");

        txtPoint.setText(point +"p");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNetworkData();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
    }

    void getNetworkData(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(PointActivity.this);
        PointApi api = retrofit.create(PointApi.class);

        // 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;

        Call<PointList> call = api.getMyPoint(accessToken,offset,limit);
        call.enqueue(new Callback<PointList>() {
            @Override
            public void onResponse(Call<PointList> call, Response<PointList> response) {
                if(response.isSuccessful()){
                    pointList.clear();
                    count = response.body().getCount();
                    pointList.addAll(response.body().getItems());
                    offset = offset + count;
                    adapter = new PointAdapter(PointActivity.this,pointList);
                    recyclerView.setAdapter(adapter);
                }else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<PointList> call, Throwable t) {

            }
        });
    }

    void addNetworkData(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(PointActivity.this);
        PointApi api = retrofit.create(PointApi.class);
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<PointList> call = api.getMyPoint(accessToken,offset,limit+5); // limit 값을 5 증가시켜서 추가 데이터를 가져옵니다.
        call.enqueue(new Callback<PointList>() {
            @Override
            public void onResponse(Call<PointList> call, Response<PointList> response) {
                if(response.isSuccessful()){
                    count = response.body().getCount();
                    pointList.addAll(response.body().getItems());
                    offset = offset + count;
                    adapter.notifyDataSetChanged(); // 어댑터에 데이터셋이 변경되었음을 알려줍니다.
                }else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<PointList> call, Throwable t) {

            }
        });

    }

}