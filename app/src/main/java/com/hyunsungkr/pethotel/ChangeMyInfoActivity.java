package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeMyInfoActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgProfile;
    ImageView imgChangePassword;
    TextView txtChangeProfile;
    TextView txtLogout;
    TextView txtEmail;
    EditText editName;
    EditText editPhone;
    Button btnSave;
    TextView txtChangePassword;
    String accessToken;
    User user;

    ArrayList<User> userArrayList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_info);
        imgBack = findViewById(R.id.imgBack);
        imgProfile = findViewById(R.id.imgProfile);
        txtChangePassword = findViewById(R.id.txtChangePassword);
        txtChangeProfile = findViewById(R.id.txtChangeProfile);
        txtLogout = findViewById(R.id.txtLogout);
        txtEmail = findViewById(R.id.txtEmail);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);
        imgChangePassword = findViewById(R.id.imgChangePassword);




        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        UserCheck();


        // 로그아웃
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMyInfoActivity.this);
                builder.setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sp의 억세스 토큰 삭제
                                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.remove(Config.ACCESS_TOKEN);
                                editor.apply();
                                Intent intent = new Intent(ChangeMyInfoActivity.this,LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(ChangeMyInfoActivity.this,"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        // 뒤로가기 버튼
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        // 비밀번호 변경으로 이동
        imgChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });
        // 비밀번호 변경으로 이동
        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });


        // 저장버튼 클릭 리스너
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = editName.getText().toString().trim();
                String Phone = editPhone.getText().toString().trim();
                updateUser(accessToken, Name, Phone);

                // 변경된 정보 없을때
                if (Name.equals(user.getName()) && Phone.equals(user.getPhone())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMyInfoActivity.this);
                    builder.setMessage("변경된 사항이 없습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // 변경 할 때 띄울 다이얼로그
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMyInfoActivity.this);
                    builder.setMessage("변경하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(ChangeMyInfoActivity.this,"저장되었습니다",Toast.LENGTH_SHORT).show();
                                    finish(); // 액티비티 종료
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss(); // 다이얼로그 닫기
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


    }


    private void UserCheck() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(ChangeMyInfoActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        Call<UserRes> call = api.userCheck(accessToken);
        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if (response.isSuccessful()) {
                    UserRes userRes = response.body();
                    userArrayList.addAll( userRes.getUser() );
                    user = userArrayList.get(0);

                    // 예약자 정보 셋팅 ex) 김이름 | 010-1234-5678
                    txtEmail.setText(user.getEmail());
                    editName.setText(user.getName());
                    editPhone.setText(user.getPhone());
                    Glide.with(ChangeMyInfoActivity.this).load(user.getUserImgUrl()).into(imgProfile);


                } else {
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }
    public void updateUser(String token, String name, String phone) {
        User user = new User();
        user.setName(name);
        user.setPhone(phone);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UserApi userApi = retrofit.create(UserApi.class);
        Call<UserRes> call = userApi.userChangeName(accessToken, user);

        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.isSuccessful()){
                    // 변경된 유저 정보 받아온 후 처리
                    UserRes userRes = response.body();

                } else {
                    // 에러 처리
                    // response.errorBody() 등을 사용하여 에러 메시지를 받아올 수 있음
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                // 통신 실패 처리
            }
        });
    }

    public void ChangePassword(){
        Intent intent = new Intent(ChangeMyInfoActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
        finish();
    }

}