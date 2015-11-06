package com.codepath.instagram.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by mrucker on 11/4/15.
 */
public class ProfileFragment extends Fragment {
    private String userId;
    private SimpleDraweeView profileImage;
    private TextView tvPostCount;
    private TextView tvFollowerCount;
    private TextView tvFollowingCount;
    private TextView tvProfileFullName;
    private TextView tvUserCaption;
    private Button btnOption;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainApplication.getRestClient().getUserById(userId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                InstagramUser user = Utils.decodeUserFromJsonResponse(responseBody);
                profileImage = (SimpleDraweeView) view.findViewById(R.id.sdvProfilePageImage);
                tvPostCount = (TextView) view.findViewById(R.id.tvPostCount);
                tvFollowerCount = (TextView) view.findViewById(R.id.tvFollowerCount);
                tvFollowingCount = (TextView) view.findViewById(R.id.tvFollowingCount);
                tvProfileFullName = (TextView) view.findViewById(R.id.tvProfileFullName);
                tvUserCaption = (TextView) view.findViewById(R.id.tvUserCaption);
                btnOption = (Button) view.findViewById(R.id.btnOption);

                tvPostCount.setText(profileMetrics(String.valueOf(user.postCount), "posts"));
                tvFollowerCount.setText(profileMetrics(String.valueOf(user.followedByCount), "followers"));
                tvFollowingCount.setText(profileMetrics(String.valueOf(user.followCount), "following"));
                tvProfileFullName.setText(user.fullName);
                tvUserCaption.setText(user.bio);
                if (userId == "self") {
                    btnOption.setText("Edit Profile");
                } else {
                    btnOption.setText("Follow");
                }

                profileImage.setAspectRatio(1.0f);
                profileImage.setImageURI(Uri.parse(user.profilePictureUrl));

                PhotoGridFragment fragment = PhotoGridFragment.newInstance(user.userId, "user");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flPhotoGrid, fragment);
                ft.commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = this.getArguments().getString("userId", "");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    private SpannableStringBuilder profileMetrics(String metric, String metricType) {
        Resources res = context.getResources();
        ForegroundColorSpan blackForegroundColorSpan = new ForegroundColorSpan(
                res.getColor(R.color.black)
        );
        ForegroundColorSpan grayForegroundColorSpan = new ForegroundColorSpan(
                res.getColor(R.color.light_gray_text)
        );

        SpannableStringBuilder ssb = new SpannableStringBuilder(metric);
        ssb.setSpan(
                blackForegroundColorSpan,
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssb.setSpan(
                new TypefaceSpan("sans-serif-medium"),
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssb.append("\n");
        ssb.append(metricType);
        ssb.setSpan(
                grayForegroundColorSpan,
                ssb.length() - metricType.length(),
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssb.setSpan(
                new RelativeSizeSpan(0.8f),
                ssb.length() - metricType.length(),
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return ssb;
    }
}
