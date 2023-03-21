package com.hyunsungkr.pethotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.hyunsungkr.pethotel.adapter.HotelReviewAdapter;
import com.hyunsungkr.pethotel.adapter.PetChoiceAdapter;
import com.hyunsungkr.pethotel.adapter.SearchHotelAdapter;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.KeywordApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.KeywordResponse;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.Reservation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResSearchActivity extends AppCompatActivity {

    private SearchHotelAdapter adapter;
    private boolean isLoading = false;

    ArrayList<Hotel> hotelArrayList = new ArrayList<>();
    Hotel selectedHotel;
    ImageView imgBack;
    ImageView imgSearch;

    EditText editKeyword;
    TextView txtDate;

    TextView txtResSearch;

    RecyclerView recyclerView;

    ArrayList<Hotel> searchList = new ArrayList<>();

    String accessToken;

    String keyword;
    int offset = 0;
    int limit = 5;
    int count = 0;

    Reservation reservation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_search);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        imgBack = findViewById(R.id.imgBack);
        imgSearch = findViewById(R.id.imgSearch);
        txtResSearch = findViewById(R.id.txtResSearch);
        txtDate = findViewById(R.id.txtDate);
        editKeyword = findViewById(R.id.editKeyword);
        editKeyword.clearFocus();


        // 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Intent intent = getIntent();
        reservation = (Reservation) intent.getSerializableExtra("reservation");
        keyword = intent.getStringExtra("keyword");
        editKeyword.setText(keyword);
        txtDate.setText(reservation.getCheckInDate() + "~" + reservation.getCheckOutDate());


        // 리싸이클러뷰 띄우기
        adapter = new SearchHotelAdapter(ResSearchActivity.this, searchList);


        adapter.setOnItemClickListener(new SearchHotelAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(int index) {
                // 클릭 이벤트 처리
                Intent intent = new Intent(ResSearchActivity.this, HotelInfoActivity.class);
                intent.putExtra("hotel",  searchList.get(index));
                intent.putExtra("reservation", reservation);
                Log.i("조회",String.valueOf(searchList.get(index).getId()));
                startActivity(intent);
            }
        });

        // 백 버튼 클릭 리스너
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 기능 수행
                finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //키보드 배열 숨기기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editKeyword.getWindowToken(), 0);
                //검색어 저장
                putNetworkSearchData();

                String keyword2 = editKeyword.getText().toString().trim();
                if(!keyword.equals(keyword2)){
                    keyword = keyword2;
                    offset = 0;
                    searchList.clear();
                    getNetworkSearchData();

                    // 새로운 어댑터 객체 생성
                    adapter = new SearchHotelAdapter(ResSearchActivity.this,searchList);

                    // 새로운 데이터 적용
                    adapter.notifyDataSetChanged();

                    // RecyclerView에 새로운 어댑터 설정
                    recyclerView.setAdapter(adapter);
                }
            }
        });


        // 스크롤 리스너 등록
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                // 마지막 아이템이 보여지고 있고, 로딩 중이 아닐 때
                if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                    isLoading = true;
                    offset = offset + limit;
                    getNetworkSearchData();
                    adapter.notifyDataSetChanged();

                    // 현재 위치 유지
                    recyclerView.scrollToPosition(lastVisibleItemPosition);
                }
            }
        });


        getNetworkSearchData();
    }


    public void favoriteProcess(int index){
        selectedHotel = searchList.get(index);

        // 해당행이 이미 좋아요인지 아닌지 파악
        if(selectedHotel.getFavorite() == 0){
            // 좋아요 API 호출
            Retrofit retrofit = NetworkClient.getRetrofitClient(ResSearchActivity.this);
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
            Retrofit retrofit = NetworkClient.getRetrofitClient(ResSearchActivity.this);
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

        // 검색리스트 가져오기
    void getNetworkSearchData(){



        Retrofit retrofit = NetworkClient.getRetrofitClient(ResSearchActivity.this);
        HotelApi api = retrofit.create(HotelApi.class);
        Call<HotelList> call = api.getSearchHotel(accessToken,keyword,offset,limit);

        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                HotelList hotelList = response.body();

                searchList.addAll(hotelList.getItems());
                if (hotelList.getCount() == 0){
                    txtResSearch.setText("검색 결과가 없습니다.");
                }else{
                    txtResSearch.setText("총" +hotelList.getCount() +" 개의 검색결과");
                }

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });

        isLoading = false;
    }
    void putNetworkSearchData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(ResSearchActivity.this);
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

}