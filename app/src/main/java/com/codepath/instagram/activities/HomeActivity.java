package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.HomeFragmentStatePagerAdapter;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        HomeFragmentStatePagerAdapter adapterViewPager = new HomeFragmentStatePagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tlTabLayout);
        tabLayout.setupWithViewPager(vpPager);
    }
}
