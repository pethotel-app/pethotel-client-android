package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.UserRes;


import java.util.Map;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText editBasicPassword;
    EditText editChangePassword;
    EditText editCheckPassword;
    Button btnSave;
    String accessToken;

    String password;
    String newPassword;
    String checkPassword;

    ImageView imgBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        // 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        editChangePassword = findViewById(R.id.editPassword);
        editBasicPassword = findViewById(R.id.editBasicPassword);
        editCheckPassword = findViewById(R.id.editCheckPassword);
        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = editBasicPassword.getText().toString().trim();
                newPassword = editChangePassword.getText().toString().trim();
                checkPassword = editCheckPassword.getText().toString().trim();

                if (password.equals(newPassword)){
                    Toast.makeText(ChangePasswordActivity.this,"기존 비밀번호와 동일한 비밀번호입니다.",Toast.LENGTH_SHORT).show();

                }else{
                    TryChange();
                }


    }
    public void changePassword(String password, String newPassword) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(ChangePasswordActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("newPassword", newPassword);

        Call<UserRes> call = api.changePassword(accessToken, params);
        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if (response.isSuccessful()) {
                    // 비밀번호 변경 성공
                    Toast.makeText(ChangePasswordActivity.this,"비밀번호가 변경되었습니다",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 비밀번호 변경 실패
                    Toast.makeText(ChangePasswordActivity.this,"기존 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                // 통신 실패 처리
                Toast.makeText(ChangePasswordActivity.this,"비밀번호 변경에 실패하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 비밀번호 변경
    public void TryChange(){
        if (newPassword.equals(checkPassword) ){
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
            builder.setMessage("비밀번호를 변경하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            changePassword(password, newPassword);
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
        } else {
            // 새로운 비밀번호와 확인 비밀번호가 일치하지 않을 때
            Toast.makeText(ChangePasswordActivity.this,"새로운 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
        }
    }
});
    }

}