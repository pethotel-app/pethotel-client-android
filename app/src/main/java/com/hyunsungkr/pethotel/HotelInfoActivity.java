package com.hyunsungkr.pethotel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.adapter.HotelReviewAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.ReviewApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.HotelReview;
import com.hyunsungkr.pethotel.model.HotelReviewList;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.Reservation;
import com.hyunsungkr.pethotel.model.ReviewSummary;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotelInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HotelReviewAdapter hotelReviewAdapter;
    private List<HotelReview> hotelReviewList;

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
    public TextView txtSummary;
    public TextView txtReviewSum2;
    public TextView txtMore;
    public ImageView imgChoicePet;
    public ImageView imgHome;
    public ImageView imgBack;
    public TextView txtAvg;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    String petName = "";
    private Pet pet;
    private Hotel hotel;
    private int hotelId;
    int hotelId2;
    String accessToken;
    Hotel intentHotel;
    int favorite;

    int offset = 0;
    int limit = 5;
    int count = 0;

    ArrayList<Hotel> hotelArrayList = new ArrayList<>();

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // 유저가 선택한 반려동물 정보 가져오기
            if (result.getResultCode() == 100){
                pet = (Pet) result.getData().getSerializableExtra("pet");
                petName = pet.getName();

                txtSelectPet.setText(pet.getName());
                imgChoicePet.setImageResource(R.drawable.baseline_check_24);
            }
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

        // RecyclerView
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
        imgHome = findViewById(R.id.imgHome);
        imgBack = findViewById(R.id.imgBack);
        txtReviewSum2 = findViewById(R.id.txtReviewSum2);
        txtMore = findViewById(R.id.txtMore);
        txtSummary = findViewById(R.id.txtSummary);
        txtAvg = findViewById(R.id.txtAvg);

        // 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        // 메인에서 인텐트로 호텔 정보 받아와서 아이디 저장
        intentHotel = (Hotel) getIntent().getSerializableExtra("hotel");
        hotelId = intentHotel.getId();


        // 좋아요 설정, 해제
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favorite == 0){
                    favorite = 1;
                    imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
                    Retrofit retrofit = NetworkClient.getRetrofitClient(HotelInfoActivity.this);
                    HotelApi api = retrofit.create(HotelApi.class);


                    Call<Res> call = api.setFavorite(accessToken, hotelId);

                    call.enqueue(new Callback<Res>() {
                        @Override
                        public void onResponse(Call<Res> call, Response<Res> response) {
                            if(response.isSuccessful()){

                                hotel.setFavorite(1);


                            }else{
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<Res> call, Throwable t) {

                        }
                    });
                }else {
                    favorite = 0;
                    imgFavorite.setImageResource(R.drawable.baseline_favorite_border_24);

                    Retrofit retrofit = NetworkClient.getRetrofitClient(HotelInfoActivity.this);
                    HotelApi api = retrofit.create(HotelApi.class);


                    Call<Res> call = api.deleteFavorite(accessToken, hotelId);

                    call.enqueue(new Callback<Res>() {
                        @Override
                        public void onResponse(Call<Res> call, Response<Res> response) {
                            if(response.isSuccessful()){
                                hotel.setFavorite(0);

                            }else{

                            }
                        }

                        @Override
                        public void onFailure(Call<Res> call, Throwable t) {

                        }
                    });
                }
            }
        });

        // 홈버튼 클릭하면 메인액티비티로 이동
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelInfoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        // 뒤로가기 버튼 클릭
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 기능 수행
            }
        });

        // 리뷰 더보기 버튼 클릭 5개씩 늘어나게
        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limit = limit+5;
                getNetworkReviewData();
            }
        });

        // 호텔 정보 불러와서 셋팅
        getNetworkData();
        // 호텔 리뷰도 셋팅
        getNetworkReviewData();
        getNetworkSummary();

        // 날짜선택
        // 오늘 날짜로 시작일 초기화
        Calendar calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        txtDateStart.setText(String.format("%d-%02d-%d", startYear, startMonth+1, startDay));

        // 내일 날짜로 끝일 초기화
        calendar.add(Calendar.DATE, 1);
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH);
        txtDateEnd.setText(String.format("%d-%02d-%d", endYear, endMonth+1, endDay));

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

        // 반려동물 선택시 가져오는 코드
        imgChoicePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelInfoActivity.this, PetChoiceActivity.class);
                launcher.launch(intent);
            }
        });

        // 소형견 예약 버튼
        btnSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 반려동물정보가 없으면 선택하라고 메세지
                String chekPet = txtSelectPet.getText().toString();
                if (chekPet.equals("반려동물 선택")) {
                    Toast.makeText(HotelInfoActivity.this, "반려동물을 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(HotelInfoActivity.this,ReservationActivity.class);
                // 예약정보 저장
                Reservation reservation = new Reservation();
                reservation.setPrice(hotel.getSmall());
                reservation.setCheckInDate(txtDateStart.getText().toString());
                reservation.setCheckOutDate(txtDateEnd.getText().toString());

                // 호텔, 펫, 예약정보 인텐트로 전달
                intent.putExtra("hotel", hotel);
                intent.putExtra("pet", pet);
                intent.putExtra("reservation", reservation);
                startActivity(intent);
            }
        });

        // 중형견 예약 버튼
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 반려동물정보가 없으면 선택하라고 메세지
                String chekPet = txtSelectPet.getText().toString();
                if (chekPet.equals("반려동물 선택")) {
                    Toast.makeText(HotelInfoActivity.this, "반려동물을 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(HotelInfoActivity.this,ReservationActivity.class);
                // 예약정보 저장
                Reservation reservation = new Reservation();
                reservation.setPrice(hotel.getMedium());
                reservation.setCheckInDate(txtDateStart.getText().toString());
                reservation.setCheckOutDate(txtDateEnd.getText().toString());

                // 호텔, 펫, 예약정보 인텐트로 전달
                intent.putExtra("hotel", hotel);
                intent.putExtra("pet", pet);
                intent.putExtra("reservation", reservation);
                startActivity(intent);

            }
        });

        // 대형견 예약 버튼
        btnLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 반려동물정보가 없으면 선택하라고 메세지
                String chekPet = txtSelectPet.getText().toString();
                if (chekPet.equals("반려동물 선택")) {
                    Toast.makeText(HotelInfoActivity.this, "반려동물을 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(HotelInfoActivity.this,ReservationActivity.class);
                // 예약정보 저장
                Reservation reservation = new Reservation();
                reservation.setPrice(hotel.getLarge());
                reservation.setCheckInDate(txtDateStart.getText().toString());
                reservation.setCheckOutDate(txtDateEnd.getText().toString());

                // 호텔, 펫, 예약정보 인텐트로 전달
                intent.putExtra("hotel", hotel);
                intent.putExtra("pet", pet);
                intent.putExtra("reservation", reservation);
                startActivity(intent);

            }
        });
    }

    // 시작일 선택
    private void showStartDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                startDateSetListener,
                startYear, startMonth, startDay
        );
        datePickerDialog.show();
    }

    // 끝일 선택
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
            txtDateEnd.setText(String.format("%d-%02d-%d", endYear, endMonth+1, endDay));
        }
    };
    private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startYear = year;
            startMonth = month;
            startDay = dayOfMonth;

            // 시작일에서 하루 뒤 날짜로 계산
            Calendar calendar = Calendar.getInstance();
            calendar.set(startYear, startMonth, startDay);
            calendar.add(Calendar.DATE, 1);
            endYear = calendar.get(Calendar.YEAR);
            endMonth = calendar.get(Calendar.MONTH);
            endDay = calendar.get(Calendar.DAY_OF_MONTH);

            txtDateStart.setText(String.format("%d-%02d-%d", startYear, startMonth+1, startDay));
            txtDateEnd.setText(String.format("%d-%02d-%d", endYear, endMonth+1, endDay));
        }
    };

    void getNetworkSummary() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(HotelInfoActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewSummary> call = api.getSummaryReview(accessToken,hotelId);
        call.enqueue(new Callback<ReviewSummary>() {
            @Override
            public void onResponse(Call<ReviewSummary> call, Response<ReviewSummary> response) {
                if(response.isSuccessful()){
                    // todo: setText
                    txtSummary.setText(response.body().getSummary());

                }else{

                }
            }

            @Override
            public void onFailure(Call<ReviewSummary> call, Throwable t) {

            }
        });
    }

    // 호텔 상세정보 가져오는 네트워크
    void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(HotelInfoActivity.this);
        HotelApi api = retrofit.create(HotelApi.class);

        Call<HotelList> call = api.checkHotel(accessToken, hotelId);

        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                if (response.isSuccessful()) {
                    HotelList hotelList = response.body();
                    hotel = hotelList.getHotel();

                    // 호텔 타이틀 셋팅
                    txtHotelName.setText(hotel.getTitle());
                    // 호텔 소개 셋팅
                    txtDescription.setText(hotel.getDescription());
                    // 호텔 객실 가격 셋팅

                    DecimalFormat myFormatter = new DecimalFormat("###,###");
                    txtSmallPrice.setText(myFormatter.format(hotel.getSmall()) + "원");
                    txtMediumPrice.setText(myFormatter.format(hotel.getMedium()) + "원");
                    txtLargePrice.setText(myFormatter.format(hotel.getLarge()) + "원");
                    // 호텔 이미지 셋팅
                    Glide.with(HotelInfoActivity.this).load(hotel.getImgUrl()).into(imgHotel);
                    // 리뷰 갯수 표시
                    String reviewCnt = String.valueOf(hotel.getCnt());
                    txtReviewSum.setText("("+reviewCnt+")");
                    txtReviewSum2.setText("("+reviewCnt+")");

                    // 별점 평균 표시
                    float ratingAvg = (float) hotel.getAvg();
                    ratingBar.setRating(ratingAvg);
                    txtReviewAvg.setText(String.format("%.1f", hotel.getAvg()));
                    // 좋아요 표시
                    if (hotel.getFavorite() == 1){
                        imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
                    }

                    // 좋아요 설정, 해제를 위해 변수선언
                    favorite = hotel.getFavorite();

                    // 리뷰 별점 표시
                    txtAvg.setText("이 호텔에 대한 평균 별점은 " + String.format("%.1f", hotel.getAvg()) + "점 입니다");


                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });


    }

    // 호텔 리뷰 가져오기
    void getNetworkReviewData() {

        offset = 0;
        count = 0;

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        ReviewApi api = retrofit.create(ReviewApi.class);
        Call<HotelReviewList> call = api.checkReview(accessToken, hotelId,offset,limit);

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