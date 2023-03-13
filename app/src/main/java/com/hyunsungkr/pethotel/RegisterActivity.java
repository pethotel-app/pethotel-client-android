package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Check;
import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserRes;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {


    EditText editName;
    EditText editPhone;
    Button btnPhoneCheck;
    EditText editEmail;
    Button btnEmailCheck;
    EditText editPassword;
    EditText editPasswordCheck;
    Button btnLogin;
    String name;
    String phone = "";
    String email = "";
    String password;
    String passwordCheck;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnPhoneCheck = findViewById(R.id.btnPhoneCheck);
        editEmail = findViewById(R.id.editEmail);
        btnEmailCheck = findViewById(R.id.btnEmailCheck);
        editPassword = findViewById(R.id.editPassword);
        editPasswordCheck = findViewById(R.id.editPasswordCheck);
        btnLogin = findViewById(R.id.btnLogin);

        // 핸드폰번호 받아서 중복체크
        btnPhoneCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Check = editPhone.getText().toString().trim();

                if (Check.equals("")) {
                    Toast.makeText(RegisterActivity.this, "핸드폰 번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                CheckPhone(Check);

            }
        });

        // 이메일 받아서 중복체크
        btnEmailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Check = editEmail.getText().toString().trim();

                if (Check.equals("")) {
                    Toast.makeText(RegisterActivity.this, "이메일을을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 이메일 형식 체크
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                if (pattern.matcher(Check).matches() == false) {
                    Toast.makeText(RegisterActivity.this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                CheckEmail(Check);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 이름 받기
                name = editName.getText().toString().trim();

                if(name.equals("")) {
                    Toast.makeText(RegisterActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(phone == "") {
                    Toast.makeText(RegisterActivity.this, "핸드폰 중복체크를 클릭해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email == "") {
                    Toast.makeText(RegisterActivity.this, "이메일 중복체크를 클릭해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 확인
                password = editPassword.getText().toString().trim();
                passwordCheck = editPasswordCheck.getText().toString().trim();

                // 비밀번호 길이 5~20자리만 허용
                if (password.length() < 5 || password.length() > 20) {
                    Toast.makeText(RegisterActivity.this, "비밀번호 길이를 확인하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 확인과 일치하지 않으면 넘어가지 않게 입력
                if(password.equals(passwordCheck)) {
                } else {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 회원가입 API 호출
                register();
            }
        });

    }

    private void register() {
        showProgress("회원가입 진행중...");

        Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        User user = new User(name, phone, email, password);

        Call<UserRes> call = api.register(user);
        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                dismissProgress();
                if (response.isSuccessful()){

                    UserRes res = response.body();

                    // 억세스토큰은 api 호출할때마다 헤더에서 사용하므로
                    // 회원가입이나 로그인이 끝나면 파일로 꼭 저장해놔야한다
                    SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCE_NAME,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Config.ACCESS_TOKEN, res.getAccess_token());
                    editor.apply();

                    // 데이터를 이상없이 처리하면 메인 액티비티를 화면에 나오게 한다
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(RegisterActivity.this, "정상적으로 처리되지 않았습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                dismissProgress();
                Toast.makeText(RegisterActivity.this, "정상적으로 처리되지 않았습니다", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    public void CheckPhone(String Check) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        User user = new User();
        user.setPhone(Check);

        Call<Check> call = api.checkPhone(user);
        call.enqueue(new Callback<Check>() {
            @Override
            public void onResponse(Call<Check> call, Response<Check> response) {
                if (response.isSuccessful()){
                    Check check = response.body();
                    String phoneCheck = check.getCheck()+"";

                    if (phoneCheck.equals("true")) {
                        phoneCheckAlertDialog("해당 번호는 사용 가능합니다");
                        phone = editPhone.getText().toString().trim();
                    } else {
                        phoneCheckAlertDialog("해당 번호는 사용 불가능합니다");
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "전화번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Check> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "정상적으로 처리되지 않았습니다", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    public void CheckEmail(String Check) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        User user = new User();
        user.setEmail(Check);

        Call<Check> call = api.checkEmail(user);
        call.enqueue(new Callback<Check>() {
            @Override
            public void onResponse(Call<Check> call, Response<Check> response) {
                if (response.isSuccessful()){
                    Check check = response.body();
                    String emailCheck = check.getCheck();

                    if (emailCheck.equals("true")) {
                        emailCheckAlertDialog("해당 이메일은 사용 가능합니다");
                        email = editEmail.getText().toString().trim();
                    } else {
                        emailCheckAlertDialog("해당 이메일은 사용 불가능합니다");
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "이메일을 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Check> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "정상적으로 처리되지 않았습니다", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void phoneCheckAlertDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false);

        builder.setTitle("핸드폰 중복 체크");
        builder.setMessage(text);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            // 확인 버튼 눌렀을때 실행코드 작성
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private void emailCheckAlertDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false);

        builder.setTitle("이메일 중복 체크");
        builder.setMessage(text);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            // 확인 버튼 눌렀을때 실행코드 작성
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
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
}