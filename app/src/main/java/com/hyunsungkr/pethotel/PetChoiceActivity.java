package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hyunsungkr.pethotel.adapter.PetChoiceAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.PetApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.PetInfoList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PetChoiceActivity extends AppCompatActivity {

    private RecyclerView RecyclerView;
    private PetChoiceAdapter adapter;
    private ArrayList<Pet> petList = new ArrayList<>();
    private ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_choice);

        imgBack = findViewById(R.id.imgBack);

        RecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.setHasFixedSize(true);

        // 레트로핏으로 반려동물 데이터 받아오기
        Retrofit retrofit = NetworkClient.getRetrofitClient(PetChoiceActivity.this);
        PetApi api = retrofit.create(PetApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<PetInfoList> call = api.getPetList(accessToken);

        call.enqueue(new Callback<PetInfoList>() {
            @Override
            public void onResponse(Call<PetInfoList> call, Response<PetInfoList> response) {
                if (response.isSuccessful()) {

                    petList.addAll(response.body().getItems());
                    adapter = new PetChoiceAdapter(PetChoiceActivity.this, petList);
                    RecyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new PetChoiceAdapter.OnItemClickListener() {
                        @Override
                        public void onImageClick(int index) {
                            Pet pet = petList.get(index);
                            Intent intent = new Intent(PetChoiceActivity.this, HotelInfoActivity.class);
                            intent.putExtra("pet", pet);
                            setResult(100, intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PetInfoList> call, Throwable t) {

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 기능 수행
            }
        });
    }
}
