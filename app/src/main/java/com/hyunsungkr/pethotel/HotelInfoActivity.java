package com.hyunsungkr.pethotel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.adapter.HotelReviewAdapter;
import com.hyunsungkr.pethotel.adapter.PetChoiceAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.ReviewApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.HotelReview;
import com.hyunsungkr.pethotel.model.HotelReviewList;
import com.hyunsungkr.pethotel.model.Pet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotelInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HotelReviewAdapter hotelReviewAdapter;
    private List<HotelReview> hotelReviewList;
    Context context =this;

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
    public TextView txtSelectPet;
    public Button btnLarge;
    public Button btnSmall;
    public Button btnMedium;


    public TextView txtDateStart;
    public TextView txtDateEnd;
    public ImageView imgChoicePet;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;

    private static final int PET_CHOICE_REQUEST_CODE = 1;
    private Pet pet;
    private Hotel hotel;

    private int hotelId;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

            // 억세스 토큰이 있는지 확인
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = sp.getString(Config.ACCESS_TOKEN, "");
        if(accessToken.isEmpty()){
            Intent intent = new Intent(HotelInfoActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



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
        txtDateStart = findViewById(R.id.txtDateStart);
        txtDateEnd = findViewById(R.id.txtDateEnd);
        imgChoicePet = findViewById(R.id.imgChoicePet);
        txtSelectPet = findViewById(R.id.txtSelectPet);
        recyclerView = findViewById(R.id.recyclerView);

        // 인텐트 받아오기
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");
        hotelId = hotel.getId();
        txtHotelName.setText(hotel.getTitle());
        txtDescription.setText(hotel.getDescription());
        txtSmallPrice.setText(Integer.toString(hotel.getSmall()) + "원");
        txtMediumPrice.setText(Integer.toString(hotel.getMedium()) + "원");
        txtLargePrice.setText(Integer.toString(hotel.getLarge()) + "원");

        Glide.with(HotelInfoActivity.this).load(hotel.getImgUrl()).into(imgHotel);



        imgChoicePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelInfoActivity.this, PetChoiceActivity.class);
                startActivity(intent);

            }
        });
        // Intent에서 Pet 객체 가져오기
        pet = (Pet) getIntent().getSerializableExtra("pet");
        if (pet != null){
            txtSelectPet.setText(pet.getName());
        }else {
            txtSelectPet.setText("반려동물 선택");
        }


        btnSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelInfoActivity.this,ReservationActivity.class);
                intent.putExtra("price", hotel.getSmall());
                intent.putExtra("hotel",hotel);
                intent.putExtra("startDate", txtDateStart.getText().toString());
                intent.putExtra("endDate", txtDateEnd.getText().toString());
                intent.putExtra("pet",pet);

                startActivity(intent);


            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelInfoActivity.this,ReservationActivity.class);
                intent.putExtra("price", hotel.getMedium());
                intent.putExtra("hotel",hotel);
                intent.putExtra("startDate", txtDateStart.getText().toString());
                intent.putExtra("endDate", txtDateEnd.getText().toString());
                intent.putExtra("pet",pet);
                startActivity(intent);
            }
        });

        btnLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelInfoActivity.this,ReservationActivity.class);
                intent.putExtra("price", hotel.getLarge());
                intent.putExtra("hotel",hotel);
                intent.putExtra("startDate", txtDateStart.getText().toString());
                intent.putExtra("endDate", txtDateEnd.getText().toString());
                intent.putExtra("pet",pet);
                startActivity(intent);
            }
        });






        // 날짜선택

        // 오늘 날짜로 시작일 초기화
        Calendar calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        txtDateStart.setText(String.format("%d년 %02d월 %d일", startYear, startMonth+1, startDay));

        // 내일 날짜로 끝일 초기화
        calendar.add(Calendar.DATE, 1);
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH);
        txtDateEnd.setText(String.format("%d년 %02d월 %d일", endYear, endMonth+1, endDay));




        txtDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });

        txtDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });



        // UI 초기화


//        getNetworkData();
        getNetworkReviewData();


    }



    // 시작일 선택 메서드
    private void showStartDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                startDateSetListener,
                startYear, startMonth, startDay
        );
        datePickerDialog.show();
    }


    // 끝일 선택 메서드
    private void showEndDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                endDateSetListener,
                endYear, endMonth, endDay
        );
        datePickerDialog.show();
    }




    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            endYear = year;
            endMonth = month;
            endDay = dayOfMonth;
            txtDateEnd.setText(String.format("%d년 %02d월 %d일", endYear, endMonth+1, endDay));
        }
    };
    private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startYear = year;
            startMonth = month;
            startDay = dayOfMonth;

            // 시작일에서 하루 뒤 날짜 계산
            Calendar calendar = Calendar.getInstance();
            calendar.set(startYear, startMonth, startDay);
            calendar.add(Calendar.DATE, 1);
            endYear = calendar.get(Calendar.YEAR);
            endMonth = calendar.get(Calendar.MONTH);
            endDay = calendar.get(Calendar.DAY_OF_MONTH);

            txtDateStart.setText(String.format("%d년 %02d월 %d일", startYear, startMonth+1, startDay));
            txtDateEnd.setText(String.format("%d년 %02d월 %d일", endYear, endMonth+1, endDay));
        }
    };

    private long getMillisFromYearMonthDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


//    void getNetworkData() {
//
//        Retrofit retrofit = NetworkClient.getRetrofitClient(HotelInfoActivity.this);
//
//        HotelApi api = retrofit.create(HotelApi.class);
//        String accessToken = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY3ODA4MTUxNywianRpIjoiMmFmMjk3MmMtYjNiMC00OWE1LTkwN2MtY2RlNTNiZDIzNDc3IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6NiwibmJmIjoxNjc4MDgxNTE3fQ.VP_pcHsGR1tZHlafCV9hN0qZ6MHdVz56NMRFGGsfujQ";
//        int hotelId = 2;
//        Call<HotelList> call = api.checkHotel(accessToken, hotelId);
//
//        call.enqueue(new Callback<HotelList>() {
//            @Override
//            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
//                if (response.isSuccessful()) {
//                    HotelList hotelList = response.body();
//                    hotel = hotelList.getHotel();
//                    txtHotelName.setText(hotel.getTitle());
//                    txtDescription.setText(hotel.getDescription());
//                    txtSmallPrice.setText(Integer.toString(hotel.getSmall()) + "원");
//                    txtMediumPrice.setText(Integer.toString(hotel.getMedium()) + "원");
//                    txtLargePrice.setText(Integer.toString(hotel.getLarge()) + "원");
//
//                    Glide.with(HotelInfoActivity.this).load(hotel.getImgUrl()).into(imgHotel);
//
//
//                    Log.i("확인", hotel.getTitle());
//
//
//                }
//            }

//            @Override
//            public void onFailure(Call<HotelList> call, Throwable t) {
//
//            }
//        });


//    }
    void getNetworkReviewData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        ReviewApi api = retrofit.create(ReviewApi.class);
        String accessToken = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY3ODA4MTUxNywianRpIjoiMmFmMjk3MmMtYjNiMC00OWE1LTkwN2MtY2RlNTNiZDIzNDc3IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6NiwibmJmIjoxNjc4MDgxNTE3fQ.VP_pcHsGR1tZHlafCV9hN0qZ6MHdVz56NMRFGGsfujQ";
        int hotelId = 2;
        Call<HotelReviewList> call = api.checkReview(accessToken, hotelId);

        call.enqueue(new Callback<HotelReviewList>() {
            @Override
            public void onResponse(Call<HotelReviewList> call, Response<HotelReviewList> response) {
                if (response.isSuccessful()) {
                    HotelReviewList hotelReviewList = response.body();
                    List<HotelReview> reviewList = hotelReviewList.getItems();

                    // HotelReviewAdapter 객체 생성
                    HotelReviewAdapter hotelReviewAdapter = new HotelReviewAdapter(HotelInfoActivity.this, reviewList);

                    // RecyclerView에 Adapter 설정
                    recyclerView.setAdapter(hotelReviewAdapter);
                }
            }



            @Override
            public void onFailure(Call<HotelReviewList> call, Throwable t) {
                // 오류 처리
            }
        });




}




}