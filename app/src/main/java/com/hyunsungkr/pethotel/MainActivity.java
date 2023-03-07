package com.hyunsungkr.pethotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    public Fragment firstFragment;
    public Fragment secondFragment;
    public Fragment thirdFragment;
    public Fragment fourthFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottomNavigationView);

        firstFragment = new Home();
        secondFragment = new Map();
        thirdFragment = new Favorite();
        fourthFragment = new MyInfo();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment fragment = null;

                if (itemId == R.id.firstFragment) {
                    fragment = firstFragment;
                    getSupportActionBar().setTitle("홈");
                } else if (itemId == R.id.secondFragment) {
                    fragment = secondFragment;
                    getSupportActionBar().setTitle("지도");
                } else if (itemId == R.id.thirdFragment) {
                    fragment = thirdFragment;
                    getSupportActionBar().setTitle("찜");
                } else if (itemId == R.id.fourthFragment) {
                    fragment = fourthFragment;
                    getSupportActionBar().setTitle("MY정보");
                }

                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        } else {
            return false;
        }


    }
}
// 저장 테스트