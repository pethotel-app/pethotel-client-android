package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyunsungkr.pethotel.adapter.CouponAdapter;
import com.hyunsungkr.pethotel.api.CouponApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Coupon;
import com.hyunsungkr.pethotel.model.CouponList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CouponActivity extends AppCompatActivity {


    ImageView imgBack;
    TextView txtMore;

    RecyclerView recyclerView;
    CouponAdapter adapter;
    ArrayList<Coupon> couponList = new ArrayList<>();

    int count = 0;
    int offset = 0;
    int limit = 5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        imgBack = findViewById(R.id.imgBack);
        txtMore = findViewById(R.id.txtMore);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CouponActivity.this));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 기존에 화면에 보여지는 리사이클러뷰는 그대로 보이게 하되,
                // 더 보기 버튼 클릭시 2개씩 더 가져오게하기
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

        Retrofit retrofit = NetworkClient.getRetrofitClient(CouponActivity.this);
        CouponApi api = retrofit.create(CouponApi.class);

        // 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;

        Call<CouponList> call = api.couponList(accessToken, offset, limit);
        call.enqueue(new Callback<CouponList>() {
            @Override
            public void onResponse(Call<CouponList> call, Response<CouponList> response) {
                if(response.isSuccessful()){
                    couponList.clear();
                    count = response.body().getCount();
                    couponList.addAll(response.body().getCouponList());
                    offset = offset + count;
                    adapter = new CouponAdapter(CouponActivity.this,couponList);
                    recyclerView.setAdapter(adapter);
                }else{
                   return;
                }
            }

            @Override
            public void onFailure(Call<CouponList> call, Throwable t) {

            }
        });





    }


    // 더보기 버튼 클릭시 2개씩 더 가져오게하는 함수
    void addNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(CouponActivity.this);
        CouponApi api = retrofit.create(CouponApi.class);

        // 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<CouponList> call = api.couponList(accessToken, offset, limit);
        call.enqueue(new Callback<CouponList>() {
            @Override
            public void onResponse(Call<CouponList> call, Response<CouponList> response) {
                if (response.isSuccessful()) {
                    count = response.body().getCount();
                    List<Coupon> newCoupons = response.body().getCouponList();
                    couponList.addAll(newCoupons);
                    offset += newCoupons.size();
                    adapter.notifyDataSetChanged();
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<CouponList> call, Throwable t) {
                // handle failure
            }
        });
    }

}