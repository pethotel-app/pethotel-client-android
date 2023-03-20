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
import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FindIdActivity extends AppCompatActivity {

    EditText editName;
    EditText editPhone;
    Button btnFind;
    String email;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnFind = findViewById(R.id.btnFind);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력한 이름과 전화번호 가져오기
                String name = editName.getText().toString().trim();
                String Phone = editPhone.getText().toString().trim();

                showProgress("아이디 찾는 중...");
                Retrofit retrofit = NetworkClient.getRetrofitClient(FindIdActivity.this);
                UserApi api = retrofit.create(UserApi.class);

                User user = new User();
                user.setName(name);
                user.setPhone(Phone);

                Call<UserRes> call = api.searchId(user);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        dismissProgress();
                        if (response.isSuccessful()) {
                            UserRes res = response.body();
                            email = res.getEmail();

                            // 아이디를 나타낼 액티비티로 이동
                            Intent intent = new Intent(FindIdActivity.this, ResIdActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            Toast.makeText(FindIdActivity.this, "정보를 올바르게 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(FindIdActivity.this, "정보를 올바르게 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    // 네트워크 로직처리시에 화면에 보여주는 함수
    void showProgress(String message) {
        dialog = new ProgressDialog(FindIdActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress() {
        dialog.dismiss();
    }
}