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

    private ProgressDialog dialog;
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

        getNetworkData();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 뒤로가기 클릭시 액티비티 종료
                finish();
            }
        });

    }

    private void getNetworkData() {
        // 다이얼로그를 화면에 띄우기
        showProgress("내 쿠폰 조회중...");

        Retrofit retrofit = NetworkClient.getRetrofitClient(UseCouponActivity.this);
        CouponApi api = retrofit.create(CouponApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<CouponList> call = api.couponList(accessToken);
        call.enqueue(new Callback<CouponList>() {
            @Override
            public void onResponse(Call<CouponList> call, Response<CouponList> response) {
                // 프로그래스 다이얼로그가 있으면 나타나지않게 해준다
                dismissProgress();
                // 서버에서 보낸 응답이 200 OK일때
                if (response.isSuccessful()) {
                    couponArrayList.clear();
                    couponArrayList.addAll( response.body().getItems() );

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

                } else {
                }
            }

            @Override
            public void onFailure(Call<CouponList> call, Throwable t) {
            }
        });
    }
    // 네트워크 로직처리시에 화면에 보여주는 함수
    void showProgress(String message) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress() {
        dialog.dismiss();
    }
}