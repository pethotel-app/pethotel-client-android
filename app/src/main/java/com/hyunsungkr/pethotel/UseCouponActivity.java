package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hyunsungkr.pethotel.adapter.PetChoiceAdapter;
import com.hyunsungkr.pethotel.adapter.UseCouponAdapter;
import com.hyunsungkr.pethotel.api.CouponApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.PointApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Coupon;
import com.hyunsungkr.pethotel.model.CouponList;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.PointRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UseCouponActivity extends AppCompatActivity {
    ImageView imgBack;

    RecyclerView recyclerView;
    UseCouponAdapter adapter;
    ArrayList<Coupon> couponArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_coupon);

        imgBack = findViewById(R.id.imgBack);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UseCouponActivity.this));

        Retrofit retrofit = NetworkClient.getRetrofitClient(UseCouponActivity.this);
        CouponApi api = retrofit.create(CouponApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<CouponList> call = api.couponList(accessToken);
        call.enqueue(new Callback<CouponList>() {
            @Override
            public void onResponse(Call<CouponList> call, Response<CouponList> response) {
                // 서버에서 보낸 응답이 200 OK일때
                if (response.isSuccessful()) {
                    couponArrayList.clear();
                    couponArrayList.addAll( response.body().getCouponList() );

                    adapter = new UseCouponAdapter(UseCouponActivity.this, couponArrayList);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new UseCouponAdapter.OnItemClickListener() {
                        @Override
                        public void onImageClick(int index) {
                            Coupon coupon = couponArrayList.get(index);
                            Intent intent = new Intent(UseCouponActivity.this, ReservationActivity.class);
                            intent.putExtra("coupon", coupon);
                            setResult(101, intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CouponList> call, Throwable t) {
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 뒤로가기 클릭시 액티비티 종료
                finish();
            }
        });

    }
}