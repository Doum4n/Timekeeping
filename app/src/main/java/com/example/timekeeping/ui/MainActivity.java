package com.example.timekeeping.ui;

import android.os.Bundle;
import android.util.SparseArray;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.timekeeping.R;
import com.example.timekeeping.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private final SparseArray<String> fragmentTitles = new SparseArray<>();

    private void replaceFragment(Fragment fragment, int fragmentId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        // Cập nhật tiêu đề Toolbar
        if (fragmentTitles.get(fragmentId) != null) {
            getSupportActionBar().setTitle(fragmentTitles.get(fragmentId));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Gán tiêu đề cho từng Fragment
        fragmentTitles.put(R.id.Home, "Trang chủ");
        fragmentTitles.put(R.id.Individual, "Cá nhân");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                replaceFragment(new HomeFragment(), R.id.Home);
                return true;
            } else if (item.getItemId() == R.id.Individual) {
                replaceFragment(new individualFragment(), R.id.Individual);
                return true;
            }
            return false;
        });

        // Gán Fragment mặc định
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), R.id.Home);
        }
    }
}
