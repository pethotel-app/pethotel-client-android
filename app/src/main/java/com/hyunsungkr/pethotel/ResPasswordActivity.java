package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ResPasswordActivity extends AppCompatActivity {

    EditText editPassword1;
    EditText editPassword2;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_password);

        editPassword1 = findViewById(R.id.editPassword1);
        editPassword2 = findViewById(R.id.editPassword2);
        btnSave = findViewById(R.id.btnSave);

        String password1 = editPassword1.getText().toString().trim();
        String password2 = editPassword2.getText().toString().trim();

        // btnSave 클릭시 화면 전환 -> 로그인화면


    }
}