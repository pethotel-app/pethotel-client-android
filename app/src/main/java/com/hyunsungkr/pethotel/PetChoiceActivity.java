package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.hyunsungkr.pethotel.adapter.PetChoiceAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.PetApi;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.PetInfoList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PetChoiceActivity extends AppCompatActivity {

    private RecyclerView RecyclerView;
    private PetChoiceAdapter Adapter;
    private ArrayList<Pet> petList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_choice);

        // RecyclerView 초기화
        RecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.setHasFixedSize(true);

        // 어댑터 생성 및 설정
        RecyclerView.setAdapter(Adapter);

        // 레트로핏으로 데이터 받아오기
        Retrofit retrofit = NetworkClient.getRetrofitClient(PetChoiceActivity.this);

        PetApi api = retrofit.create(PetApi.class);

        String accessToken = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY3ODA4MTUxNywianRpIjoiMmFmMjk3MmMtYjNiMC00OWE1LTkwN2MtY2RlNTNiZDIzNDc3IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6NiwibmJmIjoxNjc4MDgxNTE3fQ.VP_pcHsGR1tZHlafCV9hN0qZ6MHdVz56NMRFGGsfujQ";
        Call<PetInfoList> call = api.getPetList(accessToken);

        call.enqueue(new Callback<PetInfoList>() {
            @Override
            public void onResponse(Call<PetInfoList> call, Response<PetInfoList> response) {
                if (response.isSuccessful()) {
                    Adapter = new PetChoiceAdapter(PetChoiceActivity.this, response.body().getItems());
                    RecyclerView.setAdapter(Adapter);


                }
            }

            @Override
            public void onFailure(Call<PetInfoList> call, Throwable t) {

            }
        });

    }
}
