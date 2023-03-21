package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyunsungkr.pethotel.adapter.PetChoiceAdapter;
import com.hyunsungkr.pethotel.adapter.SearchHotelAdapter;
import com.hyunsungkr.pethotel.adapter.SearchRankAdapter;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.KeywordApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.Keyword;
import com.hyunsungkr.pethotel.model.KeywordList;
import com.hyunsungkr.pethotel.model.KeywordResponse;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.Reservation;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgSearch;
    EditText editKeyword;

    TextView txtDateStart;
    TextView txtDateEnd;
    String startDate;
    String endDate;
    int todayYear;
    int todayMonth;
    int todayDay;
    String keyword;
    Reservation reservation;
    ArrayList<Keyword> keywordRankList = new ArrayList<>();
    int offset = 0;
    int limit = 10;


    SearchRankAdapter adapter;
    RecyclerView recyclerView;
    List<Keyword> searchRankList;

    String accessToken;
    ArrayList<Hotel> searchList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_search);
        imgBack = findViewById(R.id.imgBack);
        imgSearch = findViewById(R.id.imgSearch);
        txtDateEnd = findViewById(R.id.txtDateEnd);
        txtDateStart  = findViewById(R.id.txtDateStart);
        editKeyword = findViewById(R.id.editKeyword);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);






        // 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        // 액티비티 켜질때 날짜 디폴트값 설정(오늘과 내일로)
        DefaultDate();


        // 돋보기 이미지 누르면 검색어로 검색
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SearchActivity.this, ResSearchActivity.class);
                reservation = new Reservation();
                reservation.setCheckInDate(txtDateStart.getText().toString());
                reservation.setCheckOutDate(txtDateEnd.getText().toString());
                keyword = editKeyword.getText().toString().trim();
                putNetworkSearchData();
                intent.putExtra("reservation",reservation);
                intent.putExtra("keyword",keyword);
                startActivity(intent);

            }
        });





        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getRankNetworkDate();
    }
    private void showDatePicker() {
        // 시작일 선택창
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        Calendar selectedStart = Calendar.getInstance();
                        selectedStart.set(Calendar.YEAR, year);
                        selectedStart.set(Calendar.MONTH, month);
                        selectedStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // 요일 정보 가져오기
                        String dayOfWeek = new SimpleDateFormat("EEE", Locale.getDefault()).format(selectedStart.getTime());

                        String selectedStartDate = year + "-" + (month + 1) + "-" + dayOfMonth + " (" + dayOfWeek + ")";
                        txtDateStart.setText(selectedStartDate);

                        // 시작일이 선택된 후, 끝일 선택창 열기
                        DatePickerDialog endDatePickerDialog = new DatePickerDialog(SearchActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                        Calendar selectedEnd = Calendar.getInstance();
                                        selectedEnd.set(Calendar.YEAR, year);
                                        selectedEnd.set(Calendar.MONTH, month);
                                        selectedEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                        // 요일 정보 가져오기
                                        String dayOfWeek = new SimpleDateFormat("EEE", Locale.getDefault()).format(selectedEnd.getTime());

                                        String selectedEndDate = year + "-" + (month + 1) + "-" + dayOfMonth + " (" + dayOfWeek + ")";
                                        txtDateEnd.setText(selectedEndDate);
                                    }
                                },
                                year,
                                month,
                                dayOfMonth + 1); // 끝일은 시작일 다음 날부터 선택 가능

                        endDatePickerDialog.getDatePicker().setMinDate(selectedStart.getTimeInMillis()); // 끝일 선택창에서 시작일 이전 날짜 선택 불가능하도록 설정
                        endDatePickerDialog.setTitle("체크아웃 날짜"); // 끝일 선택창의 타이틀 수정
                        endDatePickerDialog.show();
                    }
                },
                todayYear,
                todayMonth,
                todayDay);

        // 시작일 선택창의 타이틀 설정
        startDatePickerDialog.setTitle("체크인 날짜");

        startDatePickerDialog.show();
    }
    // 요일 포함된 문자열을 생성하는 메소드
    private String getDateStringWithDayOfWeek(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("(E)"); // 요일 포함된 문자열 포맷
        try {
            Date date = dateFormat.parse(dateString);
            String dayOfWeek = dayOfWeekFormat.format(date);
            return dateString + " " + dayOfWeek;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    // 액티비티 켜질 때 날짜 디폴트값 설정
    private void DefaultDate(){
        // 화면띄울때 디폴트값 설정 오늘날짜 , 내일날짜
        // 오늘 날짜 가져오기
        Calendar today = Calendar.getInstance();
        todayYear = today.get(Calendar.YEAR);
        todayMonth = today.get(Calendar.MONTH);
        todayDay = today.get(Calendar.DAY_OF_MONTH);

        // txtDateStart 텍스트뷰에 기본값으로 오늘 날짜 설정
        startDate = todayYear + "-" + (todayMonth + 1) + "-" + todayDay;
        String startDateWithDayOfWeek = getDateStringWithDayOfWeek(startDate);
        txtDateStart.setText(startDateWithDayOfWeek);

        // txtDateEnd 텍스트뷰에 기본값으로 내일 날짜 설정
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        int tomorrowYear = tomorrow.get(Calendar.YEAR);
        int tomorrowMonth = tomorrow.get(Calendar.MONTH);
        int tomorrowDay = tomorrow.get(Calendar.DAY_OF_MONTH);
        endDate = tomorrowYear + "-" + (tomorrowMonth + 1) + "-" + tomorrowDay;
        String endDateWithDayOfWeek = getDateStringWithDayOfWeek(endDate); // 요일 포함된 문자열 생성
        txtDateEnd.setText(endDateWithDayOfWeek);
        txtDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        txtDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
    }

    // 검색어 저장
    void putNetworkSearchData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(SearchActivity.this);
        KeywordApi api = retrofit.create(KeywordApi.class);
        Call<KeywordResponse> call = api.putKeyword(keyword);
        call.enqueue(new Callback<KeywordResponse>() {
            @Override
            public void onResponse(Call<KeywordResponse> call, Response<KeywordResponse> response) {
                if (response.isSuccessful()) {
                    KeywordResponse keywordResponse = response.body();
                    String result = keywordResponse.getResult();
                    if (result.equals("success")) {

                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<KeywordResponse> call, Throwable t) {

            }
        });

    }
    // 검색어 순위 가져오기
    void getRankNetworkDate(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(SearchActivity.this);
        KeywordApi api = retrofit.create(KeywordApi.class);
        Call<KeywordList> call = api.getKeywordRank("03-21",offset,limit);

        // 비동기적으로 요청을 보낸다.
        call.enqueue(new Callback<KeywordList>() {
            @Override
            public void onResponse(Call<KeywordList> call, Response<KeywordList> response) {
                if (response.isSuccessful()) {
                    KeywordList keywordList = response.body();
                    keywordRankList.addAll(keywordList.getItems());

                    // 어댑터 생성하여 RecyclerView에 설정
                    adapter = new SearchRankAdapter(SearchActivity.this, keywordRankList);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new SearchRankAdapter.OnItemClickListener() {
                        @Override
                        public void onImageClick(int index) {
                            Keyword keyword = keywordRankList.get(index);
                            Intent intent = new Intent(SearchActivity.this, ResSearchActivity.class);
                            reservation = new Reservation();
                            reservation.setCheckInDate(txtDateStart.getText().toString());
                            reservation.setCheckOutDate(txtDateEnd.getText().toString());
                            intent.putExtra("keyword", keyword.getKeyword());
                            intent.putExtra("reservation",reservation);
                            startActivity(intent);
                        }
                    });



                }
            }

            @Override
            public void onFailure(Call<KeywordList> call, Throwable t) {

            }
        });
    }


}
