package com.codepath.instagram.networking;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class InstagramClient {
    private static final String API_BASE_URL = "https://api.instagram.com/v1/";
    private static final String CLIENT_ID = "4087714d266740e3b6d1be098eb76539";

    public static void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_BASE_URL + "media/popular?client_id=" + CLIENT_ID, responseHandler);
    }

    public static void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_BASE_URL + "media/" + mediaId + "/comments?client_id=" + CLIENT_ID, responseHandler);
    }
}
