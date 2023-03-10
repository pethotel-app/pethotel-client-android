package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotelInfoActivity extends AppCompatActivity {
    public RatingBar ratingBar;
    public ImageView imgHotel;
    public ImageView imgFavorite;
    public TextView txtHotelName;
    public TextView txtReviewAvg;
    public TextView txtReviewSum;
    public TextView txtSmall;
    public TextView txtSmallPrice;
    public TextView txtDescription;
    public TextView txtMedium;
    public TextView txtMediumPrice;
    public TextView txtLarge;
    public TextView txtLargePrice;
    public Button btnLarge;
    public Button btnSmall;
    public Button btnMedium;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

//        // 억세스 토큰이 있는지 확인
//        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
//        String accessToken = sp.getString(Config.ACCESS_TOKEN, "");
//        if(accessToken.isEmpty()){
//            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//            startActivity(intent);
//            finish();
//            return;
//        }

        // UI 초기화
        imgHotel = findViewById(R.id.imgHotel);
        imgFavorite = findViewById(R.id.imgFavorite);
        txtHotelName = findViewById(R.id.txtHotelName);
        txtReviewAvg = findViewById(R.id.txtReviewAvg);
        txtReviewSum = findViewById(R.id.txtReviewSum);
        txtSmall = findViewById(R.id.txtSmall);
        txtSmallPrice = findViewById(R.id.txtSmallPrice);
        txtMedium = findViewById(R.id.txtMedium);
        txtMediumPrice = findViewById(R.id.txtMediumPrice);
        txtLarge = findViewById(R.id.txtLarge);
        txtLargePrice = findViewById(R.id.txtLargePrice);
        btnLarge = findViewById(R.id.btnLarge);
        btnSmall = findViewById(R.id.btnSmall);
        btnMedium = findViewById(R.id.btnMedium);
        txtDescription = findViewById(R.id.txtDescription);
        ratingBar = findViewById(R.id.ratingBar);

        getNetworkData();

    }
    void getNetworkData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(HotelInfoActivity.this);

        HotelApi api = retrofit.create(HotelApi.class);
        String accessToken = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY3ODA4MTUxNywianRpIjoiMmFmMjk3MmMtYjNiMC00OWE1LTkwN2MtY2RlNTNiZDIzNDc3IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6NiwibmJmIjoxNjc4MDgxNTE3fQ.VP_pcHsGR1tZHlafCV9hN0qZ6MHdVz56NMRFGGsfujQ";
        int hotelId = 5;
        Call<HotelList> call = api.checkHotel(accessToken,hotelId);

        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {


                if(response.isSuccessful()){

                    HotelList hotelList = response.body();
                    if(hotelList != null && !hotelList.getHotel().isEmpty()){

                        Hotel hotel = hotelList.getHotel().get(0);

                        txtHotelName.setText(hotel.getTitle());


                        txtMediumPrice.setText(hotel.getSmall());
                        Log.i("체크", String.valueOf(hotel.getSmall()));

                    }



                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });



    }
}