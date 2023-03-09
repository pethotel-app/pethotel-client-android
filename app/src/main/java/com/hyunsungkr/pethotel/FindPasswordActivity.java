package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class FindPasswordActivity extends AppCompatActivity {

    EditText editEmail1;
    EditText editEmail2;
    EditText editPhone1;
    EditText editPhone2;
    EditText editCert;
    Button btnCert;
    Button btnFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        editEmail1 = findViewById(R.id.editPassword);
        editEmail2 = findViewById(R.id.editEmail2);
        editPhone1 = findViewById(R.id.editPhone1);
        editPhone2 = findViewById(R.id.editPhone2);
        editCert = findViewById(R.id.editCert);
        btnCert = findViewById(R.id.btnCert);
        btnFind = findViewById(R.id.btnLogin);


    }
}