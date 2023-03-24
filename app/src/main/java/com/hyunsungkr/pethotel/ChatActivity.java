package com.hyunsungkr.pethotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hyunsungkr.pethotel.adapter.ChatAdapter;
import com.hyunsungkr.pethotel.adapter.CouponAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.ChatDTO;
import com.hyunsungkr.pethotel.model.Coupon;
import com.hyunsungkr.pethotel.model.UserMyPage;
import com.hyunsungkr.pethotel.model.UserMyPageRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatActivity extends AppCompatActivity {

    Button btnSend;
    String accessToken;
    EditText editText;

    public String message;

    public String userName;

    public String userId;
    String adminId = "55";
    public ChatAdapter adapter;
    String chatRoomId;
    String adminName = "관리자";

    public RecyclerView recyclerView;


    public ArrayList<UserMyPage> mypageList = new ArrayList<>();
    public ArrayList<ChatDTO> chatList = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private StorageReference mStorageRef;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        // Firebase 스토리지 인스턴스 가져오기
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ChatAdapter(this, chatList);
        recyclerView.setAdapter(adapter);


        editText = findViewById(R.id.editText);
        btnSend = findViewById(R.id.btnSend);


        // 인텐트 가져오기
        chatRoomId = getIntent().getStringExtra("chatRoomId");

        // 관리자와 유저 나누기
        if (chatRoomId ==null){

            getNetworkData();
//            readUserChatData();
                btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editText.getText().toString().trim().equals("")) {
                        return;
                    }
                    ChatDTO chat = new ChatDTO(userId, editText.getText().toString().trim(), userName);
                    databaseReference.child("chat").child(userId).push().setValue(chat);
                    editText.setText("");
                }
            });

        }else{
            readAdminChatData();
            // 관리자가 들어왔을때 버튼 클릭리스너
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editText.getText().toString().trim()==null){
                        return;
                    }
                    ChatDTO chat = new ChatDTO(adminId,editText.getText().toString().trim(), adminName);
                    databaseReference.child("chat").child(chatRoomId).push().setValue(chat);
                    editText.setText("");
                }
            });
        }










    }

    void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ChatActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기


        Call<UserMyPageRes> call = api.userMypage(accessToken);
        call.enqueue(new Callback<UserMyPageRes>() {
            @Override
            public void onResponse(Call<UserMyPageRes> call, Response<UserMyPageRes> response) {
                if (response.isSuccessful()) {
                    mypageList.clear(); // 기존 데이터 삭제.
                    mypageList.addAll(response.body().getItems());
                    userId = String.valueOf(mypageList.get(0).getId());
                    Log.i("이름2", userId);
                    userName = mypageList.get(0).getName();
                    Log.i("이름", userName);

                    OpenChat();


                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserMyPageRes> call, Throwable t) {

            }
        });

    }
    private void OpenChat(){
        databaseReference.child("chat").child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                userId = dataSnapshot.child("userId").getValue(String.class);
                message = dataSnapshot.child("editText").getValue(String.class);
                userName = dataSnapshot.child("userName").getValue(String.class);
                ChatDTO chat = new ChatDTO(userId, message, userName);
                chatList.add(chat);
                adapter.notifyItemInserted(chatList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                ChatDTO chat = dataSnapshot.getValue(ChatDTO.class);
                int index = chatList.indexOf(chat);
                if(index != -1){
                    chatList.remove(index);
                    adapter.notifyItemRemoved(index);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readAdminChatData() {
        databaseReference.child("chat").child(chatRoomId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                ChatDTO chat = dataSnapshot.getValue(ChatDTO.class);
                chatList.add(chat);
                adapter.notifyItemInserted(chatList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                ChatDTO chat = dataSnapshot.getValue(ChatDTO.class);
                int index = chatList.indexOf(chat);
                if(index != -1){
                    chatList.remove(index);
                    adapter.notifyItemRemoved(index);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void readUserChatData() {
//        databaseReference.child("chat").child(userId).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
//                ChatDTO chat = dataSnapshot.getValue(ChatDTO.class);
//                chatList.add(chat);
//                adapter.notifyItemInserted(chatList.size() - 1);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                ChatDTO chat = dataSnapshot.getValue(ChatDTO.class);
//                int index = chatList.indexOf(chat);
//                if(index != -1){
//                    chatList.remove(index);
//                    adapter.notifyItemRemoved(index);
//                }
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
}







