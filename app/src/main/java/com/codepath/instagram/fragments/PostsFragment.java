package com.codepath.instagram.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.services.NetworkService;

import java.util.ArrayList;

/**
 * Created by mrucker on 10/31/15.
 */
public class PostsFragment extends Fragment {
    private ArrayList<InstagramPost> posts;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeRefreshLayout;
    private InstagramPostsAdapter adapter;
    private Context context;
    private String nextUrl;
    private final boolean[] loading = {true};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        context = container.getContext();
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        posts = new ArrayList<InstagramPost>();
        adapter = new InstagramPostsAdapter(posts, PostsFragment.this);
        RecyclerView.ItemDecoration itemDecoration = new SimpleVerticalSpacerItemDecoration(24);

        rvPosts.addItemDecoration(itemDecoration);
        rvPosts.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (loading[0]) {
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        loading[0] = false;
                        if (nextUrl != null) {
                            Intent in = new Intent(context, NetworkService.class);
                            in.putExtra("nextUrl", nextUrl);
                            context.startService(in);
                        }
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                context.startService(new Intent(context, NetworkService.class));
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        context.startService(new Intent(context, NetworkService.class));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(context).unregisterReceiver(postsReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(NetworkService.ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(postsReceiver, filter);
    }

    public void showNetworkFailureAlert() {
        new AlertDialog.Builder(context)
                .setTitle("Network Error")
                .setMessage("A network error has occurred")
                .setNeutralButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static PostsFragment newInstance() {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private BroadcastReceiver postsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            InstagramPosts posts = (InstagramPosts) intent.getSerializableExtra("posts");
            nextUrl = intent.getStringExtra("nextUrl");
            loading[0] = true;

            adapter.addAll(posts.posts);
            adapter.notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private void loadMorePosts(int page) {

    }
}
