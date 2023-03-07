package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgSearch;
    EditText editSearch;
    Button btnDate;
    ImageView imgAroundme;
    TextView txtPopular1;
    TextView txtPopular2;
    TextView txtPopular3;
    TextView txtPopular4;
    TextView txtPopular5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imgBack = findViewById(R.id.imgBack);
        imgSearch = findViewById(R.id.imgSearch);
        imgAroundme = findViewById(R.id.imgAroundme);
        editSearch = findViewById(R.id.editSearch);
        btnDate = findViewById(R.id.btnDate);
        txtPopular1 = findViewById(R.id.txtPopular1);
        txtPopular2 = findViewById(R.id.txtPopular2);
        txtPopular3 = findViewById(R.id.txtPopular3);
        txtPopular4 = findViewById(R.id.txtPopular4);
        txtPopular5 = findViewById(R.id.txtPopular5);

    }
}