package com.example.jsontest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    MainFragment mainFragment;
    ListFragment listFragment;
    RecommendFragment recommendFragment;
    MypageFragment mypageFragment;
    Button list, recommend, review, mypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        listFragment = new ListFragment();
        recommendFragment = new RecommendFragment();
        mypageFragment = new MypageFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                        return true;
                    case R.id.list:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                        return true;
                    case R.id.recommend:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, recommendFragment).commit();
                        return true;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mypageFragment).commit();
                        return true;
                }
                return false;
            }
        });

        list = findViewById(R.id.btn_main_list);
        recommend = findViewById(R.id.btn_main_menu);
        review = findViewById(R.id.btn_main_review);
        mypage = findViewById(R.id.btn_main_mypage);

        list.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        });
        recommend.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecommendFragment.class);  // 수정 필요
            startActivity(intent);
        });
        review.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);     // 수정 필요
            startActivity(intent);
        });
        mypage.setOnClickListener(v -> {
            Intent intent = new Intent(this, MypageFragment.class);     // 수정 필요
            startActivity(intent);
        });
    }
}
