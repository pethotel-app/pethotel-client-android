package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResPasswordActivity extends AppCompatActivity {

    EditText editPassword;
    EditText editPasswordCheck;
    Button btnSave;
    private ProgressDialog dialog;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_password);

        editPassword = findViewById(R.id.editPassword);
        editPasswordCheck = findViewById(R.id.editPasswordCheck);
        btnSave = findViewById(R.id.btnSave);

        // 아이디찾기 화면에서 넘어온 이메일정보 받기
        email = getIntent().getStringExtra("email");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력받은 정보 가져오기
                String password = editPassword.getText().toString().trim();
                String check = editPasswordCheck.getText().toString().trim();

                // 비밀번호 길이 5~20자리만 허용
                if (password.length() < 5 || password.length() > 20) {
                    Toast.makeText(ResPasswordActivity.this, "비밀번호 길이를 확인하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 확인과 일치하지 않으면 넘어가지 않게 입력
                if(password.equals(check)) {
                } else {
                    Toast.makeText(ResPasswordActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgress("비밀번호 변경 중...");
                Retrofit retrofit = NetworkClient.getRetrofitClient(ResPasswordActivity.this);
                UserApi api = retrofit.create(UserApi.class);

                User user = new User();
                user.setEmail(email);
                user.setNewPassword(password);

                Call<Res> call = api.changePassword(user);
                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        dismissProgress();
                        if (response.isSuccessful()) {
                            // 메인(로그인) 페이지로 이동
                            Intent intent = new Intent(ResPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else {
                        }
                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        dismissProgress();
                    }
                });

            }
        });

    }

    // 네트워크 로직처리시에 화면에 보여주는 함수
    void showProgress(String message) {
        dialog = new ProgressDialog(ResPasswordActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress() {
        dialog.dismiss();
    }

}