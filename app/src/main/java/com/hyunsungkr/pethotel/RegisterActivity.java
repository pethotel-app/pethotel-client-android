package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {


    EditText editName;
    EditText editPhone1;
    EditText editPhone2;
    EditText editCert;
    EditText editEmail1;
    EditText editEmail2;
    EditText editPassword1;
    EditText editPassword2;
    Button btnCert;
    Button btnCheck;
    Button btnJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.editEmail);
        editPhone1 = findViewById(R.id.editPhone1);
        editPhone2 = findViewById(R.id.editPhone2);
        editCert = findViewById(R.id.editCert);
        editEmail1 = findViewById(R.id.editPassword);
        editEmail2 = findViewById(R.id.editEmail2);
        editPassword1 = findViewById(R.id.editPassword1);
        editPassword2 = findViewById(R.id.editPassword2);
        btnCert = findViewById(R.id.btnCert);
        btnCheck = findViewById(R.id.btnCheck);
        btnJoin = findViewById(R.id.btnLogin);

    }
}