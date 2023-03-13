package com.hyunsungkr.pethotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ResSearchActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgSearch;
    EditText editSearch;
    Button btnDate;
    TextView txtResSearch;

    RecyclerView recyclerView;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_search);

//        imgBack = findViewById(R.id.imgBack);
//        imgSearch = findViewById(R.id.imgSearch);
//        editSearch = findViewById(R.id.editKeyword);
//        btnDate = findViewById(R.id.btnDate);
//        txtResSearch = findViewById(R.id.txtResSearch);

    }
}