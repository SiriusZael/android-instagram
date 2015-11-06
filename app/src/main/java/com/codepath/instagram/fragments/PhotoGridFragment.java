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
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mrucker on 11/1/15.
 */
public class PhotoGridFragment extends Fragment {
    private String photoType;
    private String keyword;
    private ArrayList<InstagramPost> posts;
    private PhotoGridAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyword = getArguments().getString("keyword");
        photoType = getArguments().getString("photoType");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_grid, container, false);

        posts = new ArrayList<InstagramPost>();
        return view;
    }
    private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            posts.clear();
            posts.addAll((ArrayList<InstagramPost>) Utils.decodePostsFromJsonResponse(response));
            adapter.notifyDataSetChanged();

            super.onSuccess(statusCode, headers, response);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable errorResponse) {
            super.onFailure(statusCode, headers, response, errorResponse);
        }
    };
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvPhotoGrid = (RecyclerView) view.findViewById(R.id.rvPhotoGrid);
        adapter = new PhotoGridAdapter(posts);
        rvPhotoGrid.setAdapter(adapter);
        rvPhotoGrid.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

        switch (photoType) {
            case "user":
                MainApplication.getRestClient().getRecentPostsByUserId(keyword, responseHandler);
                break;
            case "tag":
                MainApplication.getRestClient().getRecentPostsByTag(keyword, responseHandler);
                break;
            default:
                break;
        }
    }

    public static PhotoGridFragment newInstance(String name, String photoType) {
        PhotoGridFragment fragment = new PhotoGridFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", name);
        bundle.putString("photoType", photoType);
        fragment.setArguments(bundle);
        return fragment;
    }
}
