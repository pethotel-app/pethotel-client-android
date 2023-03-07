package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    Button btnKakao;
    TextView txtId;
    TextView txtPassword;
    TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editName);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnFind);
        btnKakao = findViewById(R.id.btnKakao);
        txtId = findViewById(R.id.txtId);
        txtPassword = findViewById(R.id.txtPassword);
        txtRegister = findViewById(R.id.txtRegister);

        // 연결까지 완료
        // todo : DB의 정보와 입력받은 정보를 비교하여 같으면 로그인

    }
}