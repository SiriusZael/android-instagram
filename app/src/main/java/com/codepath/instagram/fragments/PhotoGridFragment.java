package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.PhotoGridAdapter;
import com.codepath.instagram.models.InstagramPost;

import java.util.ArrayList;

/**
 * Created by mrucker on 11/1/15.
 */
public class PhotoGridFragment extends Fragment {
    private int userId;
    private String keyword;
    private ArrayList<InstagramPost> posts;
    final private static String EXTRA_USER_ID = "userId";
    final private static String EXTRA_SEARCH_TAG = "keyword";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyword = getArguments().getString(EXTRA_SEARCH_TAG, null);
        userId = getArguments().getInt(EXTRA_USER_ID, -1);
        posts = new ArrayList<InstagramPost>();

        if (keyword != null) {
//            fetchTags
        } else if (userId != -1) {
//            fetchUser
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_grid, container, false);

        RecyclerView rvPhotoGrid = (RecyclerView) view.findViewById(R.id.rvPosts);
        PhotoGridAdapter adapter = new PhotoGridAdapter(posts);
        rvPhotoGrid.setAdapter(adapter);
        rvPhotoGrid.setLayoutManager(new GridLayoutManager(container.getContext(), 3));

        return view;
    }

    public static PhotoGridFragment newInstance(int userId) {
        PhotoGridFragment fragment = new PhotoGridFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PhotoGridFragment newInstance(String keyword) {
        PhotoGridFragment fragment = new PhotoGridFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SEARCH_TAG, keyword);
        fragment.setArguments(bundle);
        return fragment;
    }
}
