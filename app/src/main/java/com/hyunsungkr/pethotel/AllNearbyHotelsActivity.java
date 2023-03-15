package com.hyunsungkr.pethotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.libraries.places.api.model.Place;
import com.hyunsungkr.pethotel.adapter.AllnearhotelAdapter;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.Res;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.HEAD;

public class AllNearbyHotelsActivity extends AppCompatActivity {

    ImageView imgBack;

    RecyclerView recyclerView;
    AllnearhotelAdapter adapter;
    ArrayList<Hotel> hotelArrayList = new ArrayList<>();

    LocationManager locationManager;
    LocationListener locationListener;

    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 10;

    int currentCount = 0;
    private double currentLat;
    private double currentLng;

    private Hotel selectedHotel;
    String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_nearby_hotels);
        imgBack = findViewById(R.id.imgBack);


        // 헤더에 들어갈 억세스토큰 가져오기

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllNearbyHotelsActivity.this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(lastPosition + 1 == totalCount){
                    addNetworkData();
                }

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 기능 수행
            }
        });

        // API 호출에 필요한 내 위치 정보를 가져오기
        // 위치를 가져오기 위해서는, 시스템서비스로부터 로케이션 매니저를 받아온다.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 로케이션 리스너를 만든다.
        // 위치가 변할 때마다 호출되는 함수 작성!
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // 위도 경도 값을 여기서 뽑아내서 우리에 맞는 코드를 작성
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();

                if(currentCount == 0){
                    getNetworkData();
                }
                currentCount = currentCount + 1;

            }
        };

        if(ActivityCompat.checkSelfPermission(AllNearbyHotelsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(AllNearbyHotelsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(AllNearbyHotelsActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return;

        }
        // 위치기반으로 GPS 정보 가져오는 코드를 실행하는 부분
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,-1,locationListener);

    }

    private void addNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(AllNearbyHotelsActivity.this);
        HotelApi api = retrofit.create(HotelApi.class);

        Call<HotelList> call = api.getNearHotel(accessToken,currentLng,currentLat,offset,limit);

        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {


                if (response.isSuccessful()) {
                    // 정상적으로 데이터 받았으니, 리사이클러뷰에 표시
                    HotelList hotelList = response.body();
                    hotelArrayList.addAll(hotelList.getItems());
                    adapter.notifyDataSetChanged();

                    // 오프셋 코드 처리
                    count = hotelList.getCount();
                    offset = offset + count;

                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
    }

    private void getNetworkData(){
        offset = 0;
        count = 0;

        Retrofit retrofit = NetworkClient.getRetrofitClient(AllNearbyHotelsActivity.this);
        HotelApi api = retrofit.create(HotelApi.class);
        Call<HotelList> call = api.getNearHotel(accessToken,currentLng,currentLat,offset,limit);
        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                hotelArrayList.clear();
                if(response.isSuccessful()){
                    count = response.body().getCount();

                    hotelArrayList.addAll(response.body().getItems());

                    offset = offset + count;

                    adapter = new AllnearhotelAdapter(AllNearbyHotelsActivity.this,hotelArrayList);
                    adapter.setOnItemClickListener(new AllnearhotelAdapter.OnItemClickListener() {
                        @Override
                        public void onCardViewClick(int index) {
                            Hotel hotel = hotelArrayList.get(index);
                            Intent intent = new Intent(AllNearbyHotelsActivity.this,HotelInfoActivity.class);
                            intent.putExtra("hotel",hotel);
                            Log.i("리뷰 갯수 확인", String.valueOf(hotel.getCnt()));


                            startActivity(intent);

                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });

    }

    public void favoriteProcess(int index){
        selectedHotel = hotelArrayList.get(index);

        // 해당행이 이미 좋아요인지 아닌지 파악
        if(selectedHotel.getFavorite() == 0){
            // 좋아요 API 호출
            Retrofit retrofit = NetworkClient.getRetrofitClient(AllNearbyHotelsActivity.this);
            HotelApi api = retrofit.create(HotelApi.class);
            Call<Res> call = api.setFavorite(accessToken, selectedHotel.getId());
            call.enqueue(new Callback<Res>() {
                @Override
                public void onResponse(Call<Res> call, Response<Res> response) {
                    if(response.isSuccessful()){
                        selectedHotel.setFavorite(1);
                        adapter.notifyDataSetChanged();
                    }else{
                        return;
                    }
                }

                @Override
                public void onFailure(Call<Res> call, Throwable t) {

                }
            });

        } else {
            // 찜 해제 API 호출
            Retrofit retrofit = NetworkClient.getRetrofitClient(AllNearbyHotelsActivity.this);
            HotelApi api = retrofit.create(HotelApi.class);
            Call<Res> call = api.deleteFavorite(accessToken, selectedHotel.getId());
            call.enqueue(new Callback<Res>() {
                @Override
                public void onResponse(Call<Res> call, Response<Res> response) {
                    if(response.isSuccessful()){
                        selectedHotel.setFavorite(0);
                        adapter.notifyDataSetChanged();
                    }else{

                    }
                }

                @Override
                public void onFailure(Call<Res> call, Throwable t) {

                }
            });

        }

    }

}