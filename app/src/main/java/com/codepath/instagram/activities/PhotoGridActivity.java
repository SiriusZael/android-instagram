package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
        String photoType = getIntent().getStringExtra("photoType");
        fragment = PhotoGridFragment.newInstance(keyword, photoType);

        ft.replace(R.id.flPhotoGrid, fragment);
        ft.commit();
    }

}
