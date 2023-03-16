package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hyunsungkr.pethotel.api.CouponApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Coupon;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.UserRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        imgBack = findViewById(R.id.imgBack);
        imgCoupon = findViewById(R.id.imgCoupon);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 기능 수행
            }
        });

        imgCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
                // 이 다이얼로그의 외곽부분을 클릭했을때 사라지지 않도록 하는 코드
                builder.setCancelable(false);

                builder.setTitle("오픈기념 이벤트");
                builder.setMessage("2,000원 오픈기념 이벤트 쿠폰을 \n쿠폰함에 쏙 넣어드렸어요~!");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인버튼 눌렀을때 실행코드 작성
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addCoupon();
                    }
                });
                builder.show();
            }
        });
    }

    private void addCoupon() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(EventActivity.this);
        CouponApi api = retrofit.create(CouponApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<Res> call = api.addCoupon(32, accessToken);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {

                } else {
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }

}