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
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by mrucker on 10/29/15.
 */
public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.CommentsViewHolder> {
    private List<InstagramComment> comments;
    private Context context;

    public InstagramCommentsAdapter(List<InstagramComment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View commentView = LayoutInflater.from(context).inflate(R.layout.layout_item_comment, parent, false);

        CommentsViewHolder viewHolder = new CommentsViewHolder(commentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        InstagramComment comment = comments.get(position);
        String timestamp = (String) DateUtils.getRelativeTimeSpanString(comment.createdTime * 1000);
        Uri profileImageUri = Uri.parse(comment.user.profilePictureUrl);

        holder.tvComment.setText(userCommentStyle(comment.user.userName, comment.text));
        holder.tvCommentTimestamp.setText(timestamp);

        holder.sdvCommentProfileImage.setAspectRatio(1.0f);
        holder.sdvCommentProfileImage.setImageURI(profileImageUri);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvComment;
        public TextView tvCommentTimestamp;
        public SimpleDraweeView sdvCommentProfileImage;

        public CommentsViewHolder(View layoutView) {
            super(layoutView);
            tvComment = (TextView) layoutView.findViewById(R.id.tvComment);
            tvCommentTimestamp = (TextView) layoutView.findViewById(R.id.tvCommentTimestamp);
            sdvCommentProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvCommentProfileImage);
        }
    }

    private SpannableStringBuilder userCommentStyle(String userName, String comment) {
        Resources res = context.getResources();
        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(
                res.getColor(R.color.blue_text)
        );
        ForegroundColorSpan grayForegroundColorSpan = new ForegroundColorSpan(
                res.getColor(R.color.gray_text)
        );

        SpannableStringBuilder ssb = new SpannableStringBuilder(userName);
        ssb.setSpan(
                blueForegroundColorSpan,
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssb.append(" ");
        ssb.append(comment);
        ssb.setSpan(
                grayForegroundColorSpan,
                ssb.length() - comment.length(),
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return ssb;
    }
}
