package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResIdActivity extends AppCompatActivity {

    TextView txtResEmail;
    Button btnLogin;
    Button btnFindPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_id);

        txtResEmail = findViewById(R.id.txtResEmail);
        btnLogin = findViewById(R.id.btnLogin);
        btnFindPassword = findViewById(R.id.btnFindPassword);

        // 아이디찾기 화면에서 넘어온 이메일정보 받기
        String email = getIntent().getStringExtra("email");

        txtResEmail.setText(email);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인화면 띄우기
                Intent intent = new Intent(ResIdActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 비밀번호 찾기 화면 띄우기
                Intent intent = new Intent(ResIdActivity.this, FindPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}