package com.hyunsungkr.pethotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hyunsungkr.pethotel.adapter.ChatRoomListAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.UserMyPage;
import com.hyunsungkr.pethotel.model.UserMyPageRes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdminActivity extends AppCompatActivity {
    private ChatRoomListAdapter adapter;
    private List<String> mChatRoomIds = new ArrayList<>();
    String accessToken;
    String userId;
    String userName;
    public ArrayList<UserMyPage> mypageList = new ArrayList<>();
    RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminActivity.this));
        recyclerView.setHasFixedSize(true);

        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> mChatRoomIds = new ArrayList<>();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String chatId = chatSnapshot.getKey();
                    mChatRoomIds.add(chatId);

                }
                adapter = new ChatRoomListAdapter(AdminActivity.this,mChatRoomIds);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}