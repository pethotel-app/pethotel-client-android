package com.hyunsungkr.pethotel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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

    private static final int resultCode = 1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

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

        // 선택된 Pet 객체 받기
        Intent intent = getIntent();
        Pet selectedPet = intent.getParcelableExtra("selectedPet");

        // 선택된 Pet 객체의 이름을 표시

        imgChoicePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelInfoActivity.this, PetChoiceActivity.class);
                startActivityForResult(intent, resultCode);

            }
        });


        // 날짜선택

        Calendar calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);

        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH) + 1;


        txtDateStart = findViewById(R.id.txtDateStart);
        txtDateStart.setText(String.format("%d년 %d월 %d일", startYear, startMonth + 1, startDay));
        txtDateEnd = findViewById(R.id.txtDateEnd);
        txtDateEnd.setText(String.format("%d년 %d월 %d일", startYear, startMonth + 1, startDay + 1));

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


        getNetworkData();


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
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, startMonth, startDay);
        calendar.add(Calendar.DATE, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            endYear = year;
            endMonth = month;
            endDay = dayOfMonth;

            // 선택된 날짜가 시작일 이전인 경우, 끝일을 시작일과 같은 날짜로 설정
            if (endYear < startYear || endYear == startYear && endMonth < startMonth
                    || endYear == startYear && endMonth == startMonth && endDay < startDay) {
                endYear = startYear;
                endMonth = startMonth;
                endDay = startDay;
            }

            txtDateEnd.setText(String.format("%d년 %02d월 %d일", endYear, endMonth, endDay));

        }
    };
    private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startYear = year;
            startMonth = month;
            startDay = dayOfMonth;

            // 선택된 날짜가 끝일 이후인 경우, 시작일을 끝일과 같은 날짜로 설정
            if (startYear > endYear || startYear == endYear && startMonth > endMonth
                    || startYear == endYear && startMonth == endMonth && startDay > endDay) {
                startYear = endYear;
                startMonth = endMonth;
                startDay = endDay;
            }

            // 월 값에 1을 더하여 표시되는 월을 수정
            txtDateEnd.setText(String.format("%d년 %d월 %d일", endYear, endMonth + 1, endDay));

        }
    };

    private long getMillisFromYearMonthDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month + 1, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(HotelInfoActivity.this);

        HotelApi api = retrofit.create(HotelApi.class);
        String accessToken = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY3ODA4MTUxNywianRpIjoiMmFmMjk3MmMtYjNiMC00OWE1LTkwN2MtY2RlNTNiZDIzNDc3IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6NiwibmJmIjoxNjc4MDgxNTE3fQ.VP_pcHsGR1tZHlafCV9hN0qZ6MHdVz56NMRFGGsfujQ";
        int hotelId = 5;
        Call<HotelList> call = api.checkHotel(accessToken, hotelId);

        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                if (response.isSuccessful()) {
                    HotelList hotelList = response.body();
                    Hotel hotel = hotelList.getHotel();
                    txtHotelName.setText(hotel.getTitle());
                    txtDescription.setText(hotel.getDescription());
                    txtSmallPrice.setText(Integer.toString(hotel.getSmall()) + "원");
                    txtMediumPrice.setText(Integer.toString(hotel.getMedium()) + "원");
                    txtLargePrice.setText(Integer.toString(hotel.getLarge()) + "원");

                    Glide.with(HotelInfoActivity.this).load(hotel.getImgUrl()).into(imgHotel);


                    Log.i("확인", hotel.getTitle());


                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });


    }

    // 선택된 Pet 객체를 전달 받는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // 선택된 Pet 객체 받기
            Pet selectedPet = data.getParcelableExtra("selectedPet");

            // 선택된 Pet 객체의 이름을 표시
            txtSelectPet.setText(selectedPet.getName());
        }
    }
}