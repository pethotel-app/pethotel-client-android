package com.hyunsungkr.pethotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyunsungkr.pethotel.adapter.FavoriteAdapter;
import com.hyunsungkr.pethotel.adapter.HotelReviewAdapter;
import com.hyunsungkr.pethotel.adapter.MyReviewAdapter;
import com.hyunsungkr.pethotel.api.FavoriteApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.ReviewApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.HotelReview;
import com.hyunsungkr.pethotel.model.HotelReviewList;
import com.hyunsungkr.pethotel.model.MyReviewList;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyReviewActivity extends AppCompatActivity {

    ImageView imgBack;
    RecyclerView recyclerView;
    MyReviewAdapter adapter;
    ArrayList<Review> reviewArrayList = new ArrayList<>();
    String accessToken;
    int offset = 0;
    int count = 0;
    int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        imgBack = findViewById(R.id.imgBack);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyReviewActivity.this));

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        // 내 리뷰 데이터 가져오기
        getNetworkData();

        // 페이징처리
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

        // 뒤로가기 버튼
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    void getNetworkData(){

        offset = 0;
        count = 0;

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<MyReviewList> call = api.myReview(accessToken, offset, limit);
        call.enqueue(new Callback<MyReviewList>() {
            @Override
            public void onResponse(Call<MyReviewList> call, Response<MyReviewList> response) {
                reviewArrayList.clear();
                if (response.isSuccessful()) {
                    MyReviewList myReviewList = response.body();
                    reviewArrayList.addAll(myReviewList.getItems());

                    count = response.body().getCount();
                    offset = offset + count;

                    adapter = new MyReviewAdapter(MyReviewActivity.this, reviewArrayList);
                    adapter.setOnItemClickListener(new MyReviewAdapter.OnItemClickListener() {
                        @Override
                        public void onCardViewClick(int index) {
                            Review review = reviewArrayList.get(index);
                            Hotel hotel = new Hotel();
                            hotel.setId(review.getHotelId());
                            Intent intent = new Intent(MyReviewActivity.this, HotelInfoActivity.class);
                            intent.putExtra("hotel", hotel);
                            startActivity(intent);
                        }
                    });

                    recyclerView.setAdapter(adapter);
                } else {

                }
            }
            @Override
            public void onFailure(Call<MyReviewList> call, Throwable t) {
            }
        });
    }

    private void addNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<MyReviewList> call = api.myReview(accessToken, offset, limit);
        call.enqueue(new Callback<MyReviewList>() {
            @Override
            public void onResponse(Call<MyReviewList> call, Response<MyReviewList> response) {
                if (response.isSuccessful()) {
                    MyReviewList myReviewList = response.body();
                    reviewArrayList.addAll(myReviewList.getItems());
                    adapter.notifyDataSetChanged();

                    // 오프셋 코드 처리
                    count = myReviewList.getCount();
                    offset = offset + count;

                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<MyReviewList> call, Throwable t) {

            }
        });
    }

    public void deleteReview(int index) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        ReviewApi api = retrofit.create(ReviewApi.class);
        Review review = reviewArrayList.get(index);

        Call<Res> call = api.deleteReview(accessToken, review.getHotelId());
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyReviewActivity.this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                    reviewArrayList.remove(index);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MyReviewActivity.this, "정상동작하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Toast.makeText(MyReviewActivity.this, "정상동작하지 않습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

}