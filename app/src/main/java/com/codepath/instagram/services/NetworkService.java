package com.codepath.instagram.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.networking.InstagramClient;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mrucker on 11/2/15.
 */
public class NetworkService extends IntentService {
    public static final String ACTION = "com.mrucker.servicesdemo.NetworkService";

    public NetworkService() {
        super("network-service");
    }

    public NetworkService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final InstagramPosts serializedPosts = new InstagramPosts();
        final Intent in = new Intent(ACTION);
        final InstagramClientDatabase database = InstagramClientDatabase.getInstance(this);

        if (!isNetworkAvailable()) {
            serializedPosts.posts = (ArrayList<InstagramPost>) database.getAllInstagramPosts();
            in.putExtra("posts", serializedPosts);
            LocalBroadcastManager.getInstance(NetworkService.this).sendBroadcast(in);
        } else {
            SyncHttpClient syncClient = new SyncHttpClient();
            InstagramClient client = MainApplication.getRestClient();

            RequestParams params = new RequestParams("access_token", client.checkAccessToken().getToken());
            syncClient.get(this, InstagramClient.REST_URL + "users/self/feed", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                    ArrayList<InstagramPost> newPosts = (ArrayList<InstagramPost>) Utils.decodePostsFromJsonResponse(responseBody);
                    serializedPosts.posts = newPosts;

                    database.emptyAllTables();
                    database.addInstagramPosts(newPosts);

                    in.putExtra("posts", serializedPosts);
                    LocalBroadcastManager.getInstance(NetworkService.this).sendBroadcast(in);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
