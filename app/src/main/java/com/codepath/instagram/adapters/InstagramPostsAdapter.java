package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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

        View postView = LayoutInflater.from(context).inflate(R.layout.layout_item_post, parent, false);

        PostsViewHolder viewHolder = new PostsViewHolder(postView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        final InstagramPost post = this.posts.get(position);

        final Uri postImageUri = Uri.parse(post.image.imageUrl);
        Uri profileImageUri = Uri.parse(post.user.profilePictureUrl);
        String userName = post.user.userName;
        String caption = post.caption;
        Resources res = context.getResources();
        String likeText = res.getQuantityString(R.plurals.like_messages, post.likesCount, Utils.formatNumberForDisplay(post.likesCount));
        String timestamp = (String) DateUtils.getRelativeTimeSpanString(post.createdTime * 1000);

        holder.tvLikesCount.setText(likeText);
        holder.tvUserName.setText(userName);
        holder.tvTimestamp.setText(timestamp);

        if (post.caption == null) {
            holder.tvCaption.setVisibility(View.GONE);
        } else {
            holder.tvCaption.setVisibility(View.VISIBLE);
            holder.tvCaption.setText(userCommentStyle(userName, caption), TextView.BufferType.EDITABLE);
        }

        holder.sdvPostImage.setAspectRatio(1.0f);
        holder.sdvPostImage.setImageURI(postImageUri);

        holder.sdvProfileImage.setAspectRatio(1.0f);
        holder.sdvProfileImage.setImageURI(profileImageUri);

        holder.llComments.removeAllViews();
        if (post.commentsCount > 2) {
            TextView moreComments = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_more_comments, null);
            moreComments.setText(res.getString(R.string.more_comments, post.commentsCount));
            moreComments.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("mediaId", post.mediaId);
                    context.startActivity(intent);
                }
            });
            holder.llComments.addView(moreComments);
        } else if (post.commentsCount == 0) {
            holder.llComments.setVisibility(View.GONE);
        }

        for (int i = post.comments.size() - 2; i < post.comments.size(); i++) {
            TextView commentView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, null);
            InstagramComment comment = post.comments.get(i);
            String commentUserName = comment.user.userName;
            String commentText = comment.text;
            commentView.setText(userCommentStyle(commentUserName, commentText), TextView.BufferType.EDITABLE);
            holder.llComments.addView(commentView);
        }

        holder.ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup(view, postImageUri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void showFilterPopup(View v, final Uri postImageUri) {
        PopupMenu popup = new PopupMenu(context, v);

        popup.getMenuInflater().inflate(R.menu.popup_filter, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_share:

                        ImagePipeline imagePipeline = Fresco.getImagePipeline();

                        ImageRequest imageRequest = ImageRequestBuilder
                                .newBuilderWithSource(postImageUri)
                                .setRequestPriority(Priority.HIGH)
                                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                                .build();

                        DataSource<CloseableReference<CloseableImage>> dataSource =
                                imagePipeline.fetchDecodedImage(imageRequest, context);

                        try {
                            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                @Override
                                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                    if (bitmap == null) {
                                        return;
                                    }

                                    shareBitmap(bitmap);
                                }

                                @Override
                                public void onFailureImpl(DataSource dataSource) {

                                }
                            }, CallerThreadExecutor.getInstance());
                        } finally {
                            if (dataSource != null) {
                                dataSource.close();
                            }
                        }


                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }

    public void shareBitmap(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap, "Image Description", null);

        Uri bmpUri = Uri.parse(path);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName;
        public TextView tvLikesCount;
        public TextView tvTimestamp;
        public TextView tvCaption;
        public SimpleDraweeView sdvPostImage;
        public SimpleDraweeView sdvProfileImage;
        public LinearLayout llComments;
        public ImageButton ibMenu;

        public PostsViewHolder(View layoutView) {
            super(layoutView);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            tvLikesCount = (TextView) layoutView.findViewById(R.id.tvLikesCount);
            tvTimestamp = (TextView) layoutView.findViewById(R.id.tvTimestamp);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
            sdvPostImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvPostImage);
            sdvProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvProfileImage);
            llComments = (LinearLayout) layoutView.findViewById(R.id.llComments);
            ibMenu = (ImageButton) layoutView.findViewById(R.id.ibMenu);
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
