package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by mrucker on 10/27/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostsViewHolder> {
    private List<InstagramPost> posts;
    private Context context;

    public InstagramPostsAdapter(List<InstagramPost> posts) {
        this.posts = posts;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.layout_item_post, parent, false);

        PostsViewHolder viewHolder = new PostsViewHolder(postView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        InstagramPost post = this.posts.get(position);

        Uri postImageUri = Uri.parse(post.image.imageUrl);
        Uri profileImageUri = Uri.parse(post.user.profilePictureUrl);
        Resources res = context.getResources();
        String userName = post.user.userName;
        String caption = post.caption;
        String likeText = res.getQuantityString(R.plurals.like_messages, post.likesCount, Utils.formatNumberForDisplay(post.likesCount));
        String timestamp = (String) DateUtils.getRelativeTimeSpanString(post.createdTime * 1000);
        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(
                res.getColor(R.color.blue_text)
        );
        ForegroundColorSpan grayForegroundColorSpan = new ForegroundColorSpan(
                res.getColor(R.color.gray_text)
        );

        holder.tvLikesCount.setText(likeText);
        holder.tvUserName.setText(userName);
        holder.tvTimestamp.setText(timestamp);

        if (post.caption == null) {
            holder.tvCaption.setVisibility(View.GONE);
        } else {
            SpannableStringBuilder ssb = new SpannableStringBuilder(userName);
            ssb.setSpan(
                    blueForegroundColorSpan,
                    0,
                    ssb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            ssb.append(" ");
            ssb.append(caption);
            ssb.setSpan(
                    grayForegroundColorSpan,
                    ssb.length() - caption.length(),
                    ssb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            holder.tvCaption.setVisibility(View.VISIBLE);
            holder.tvCaption.setText(ssb, TextView.BufferType.EDITABLE);
        }

        holder.sdvPostImage.setAspectRatio(1.0f);
        holder.sdvPostImage.setImageURI(postImageUri);

        holder.sdvProfileImage.setAspectRatio(1.0f);
        holder.sdvProfileImage.setImageURI(profileImageUri);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName;
        public TextView tvLikesCount;
        public TextView tvTimestamp;
        public TextView tvCaption;
        public SimpleDraweeView sdvPostImage;
        public SimpleDraweeView sdvProfileImage;

        public PostsViewHolder(View layoutView) {
            super(layoutView);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            tvLikesCount = (TextView) layoutView.findViewById(R.id.tvLikesCount);
            tvTimestamp = (TextView) layoutView.findViewById(R.id.tvTimestamp);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
            sdvPostImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvPostImage);
            sdvProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvProfileImage);
        }
    }
}
