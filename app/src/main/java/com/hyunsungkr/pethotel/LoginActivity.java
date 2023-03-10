package com.hyunsungkr.pethotel;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserRes;
import com.kakao.sdk.user.UserApiClient;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    TextView txtId;
    TextView txtPassword;
    TextView txtRegister;
    ImageView imgKakao;
    private ProgressDialog dialog;
    User setUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtId = findViewById(R.id.txtId);
        txtPassword = findViewById(R.id.txtPassword);
        txtRegister = findViewById(R.id.txtRegister);
        imgKakao = findViewById(R.id.imgKakao);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();

                Pattern pattern = Patterns.EMAIL_ADDRESS;
                if (pattern.matcher(email).matches() == false) {
                    Toast.makeText(LoginActivity.this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = editPassword.getText().toString().trim();

                if (password.length() < 5 || password.length() > 20) {
                    Toast.makeText(LoginActivity.this, "비밀번호 길이를 확인하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgress("로그인 중..");

                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
                UserApi api = retrofit.create(UserApi.class);

                User user = new User(email, password);

                Call<UserRes> call = api.login(user);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        dismissProgress();
                        if (response.isSuccessful()){
                            UserRes res = response.body();

                            SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Config.ACCESS_TOKEN, res.getAccess_token());
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (response.code() == 400) {
                            Toast.makeText(LoginActivity.this, "회원가입이 되어있지 않거나 비번이 틀렸습니다", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Log.i("response" , String.valueOf(response.code()));
                            Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 액티비티 띄우기
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아이디 찾기 액티비티 띄우기
                Intent intent = new Intent(LoginActivity.this, FindIdActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 비밀번호 찾기 액티비티 띄우기
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카카오 로그인 구현
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                    login();
                }
                else {
                    accountLogin();
                }
            }
        });
    }

    // 네트워크 로직처리시에 화면에 보여주는 함수
    void showProgress(String message) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress() {
        dialog.dismiss();
    }
    public void login(){
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                // 로그인 성공시 사용자 정보 받아오기
                getUserInfo();
            }
            return null;
        });
    }

    public void accountLogin(){
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                // 로그인 성공시 사용자 정보 받아오기
                getUserInfo();
            }
            return null;
        });
    }
    public void getUserInfo(){
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            // 사용자 정보 요청 실패
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                // 사용자 정보 요청(성공 정보 받아오기)
                setUser = new User();
                setUser.setEmail(user.getKakaoAccount().getEmail());
                setUser.setName(user.getKakaoAccount().getProfile().getNickname());

                // 받아온 정보로 회원 등록
                setUserInfo();
            }
            return null;
        });
    }

    public void setUserInfo() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        User user = new User(setUser.getName(), "", setUser.getEmail(), "");

        Call<UserRes> call = api.register(user);
        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if (response.isSuccessful()){
                    UserRes res = response.body();

                    SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Config.ACCESS_TOKEN, res.getAccess_token());
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }
}