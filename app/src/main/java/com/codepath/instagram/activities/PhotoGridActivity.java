package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PhotoGridFragment;

public class PhotoGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PhotoGridFragment fragment;

        String keyword = getIntent().getStringExtra("keyword");
        int userId = getIntent().getIntExtra("userId", -1);

        if (keyword != null) {
            fragment = PhotoGridFragment.newInstance(keyword);
        } else if (userId != -1) {
            fragment = PhotoGridFragment.newInstance(userId);
        } else {
            fragment = (PhotoGridFragment) new Fragment();
        }

        ft.replace(R.id.flPhotoGrid, fragment);
        ft.commit();
    }

}
