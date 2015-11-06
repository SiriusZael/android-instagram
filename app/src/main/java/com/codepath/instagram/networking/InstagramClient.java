package com.codepath.instagram.networking;

import android.content.Context;

import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class InstagramClient extends OAuthBaseClient {
    public static final String REST_URL = "https://api.instagram.com/v1/";
    private static final String REST_CONSUMER_KEY = "7fb830b3d9f944caadde9827cacc50a2";
    private static final Class REST_API_CLASS = InstagramApi.class;
    private static final String REST_CONSUMER_SECRET = "eaaed381d5264040930f3930937c2aa2";
    private static final String REDIRECT_URI = Constants.REDIRECT_URI;
    private static final String SCOPE = Constants.SCOPE;

    public InstagramClient(Context c) {
        super(c, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REDIRECT_URI, SCOPE);
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "media/popular", responseHandler);
    }

    public void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "media/" + mediaId + "/comments", responseHandler);
    }

    public void getOwnFeed(JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "users/self/feed", responseHandler);
    }

    public void searchUsersByName(String name, JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "users/search?q=" + name, responseHandler);
    }

    public void searchTagsByKeyword(String keyword, JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "tags/search?q=" + keyword, responseHandler);
    }

    public void getRecentPostsByTag(String keyword, JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "tags/" + keyword + "/media/recent/", responseHandler);
    }

    public void getRecentPostsByUserId(String userId, JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "users/" + userId + "/media/recent/", responseHandler);
    }

    public void getUserById(String userId, JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "users/" + userId, responseHandler);
    }
}
