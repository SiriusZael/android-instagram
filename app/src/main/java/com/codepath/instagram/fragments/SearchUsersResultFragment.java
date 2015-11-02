package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramUsersAdapter;
import com.codepath.instagram.models.InstagramUser;

import java.util.ArrayList;

/**
 * Created by mrucker on 10/31/15.
 */
public class SearchUsersResultFragment extends Fragment implements SearchFragment.SearchUsersFragmentListener {
    private ArrayList<InstagramUser> users;
    private RecyclerView rvSearchResults;
    private InstagramUsersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_search, container, false);

        rvSearchResults = (RecyclerView) view.findViewById(R.id.rvUserSearchResults);
        adapter = new InstagramUsersAdapter(users);
        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(null));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<InstagramUser>();
    }


    public static SearchUsersResultFragment newInstance() {
        SearchUsersResultFragment fragment = new SearchUsersResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onUsersLoaded(ArrayList<InstagramUser> users) {
        this.users = users;
        adapter.notifyDataSetChanged();
    }
}
