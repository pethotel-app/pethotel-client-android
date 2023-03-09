package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class FindIdActivity extends AppCompatActivity {

    EditText editCert;
    EditText editName;
    EditText editPhone1;
    EditText editPhone2;
    Button btnCert;
    Button btnFind;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        editCert = findViewById(R.id.editCert);
        editName = findViewById(R.id.editEmail);
        editPhone1 = findViewById(R.id.editPhone1);
        editPhone2 = findViewById(R.id.editPhone2);
        btnCert = findViewById(R.id.btnPhoneCheck);
        btnFind = findViewById(R.id.btnLogin);



    }
}