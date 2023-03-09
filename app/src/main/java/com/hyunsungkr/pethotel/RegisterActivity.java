package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {


    EditText editName;
    EditText editPhone;
    Button btnPhoneCheck;
    EditText editEmail;
    Button btnEmailCheck;
    EditText editPassword;
    EditText editPasswordCheck;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        btnPhoneCheck = findViewById(R.id.btnPhoneCheck);
        editEmail = findViewById(R.id.editEmail);
        btnEmailCheck = findViewById(R.id.btnEmailCheck);
        editPassword = findViewById(R.id.editPassword);
        editPasswordCheck = findViewById(R.id.editPasswordCheck);
        btnLogin = findViewById(R.id.btnLogin);




    }
}