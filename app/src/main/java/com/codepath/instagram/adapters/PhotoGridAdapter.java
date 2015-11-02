package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by mrucker on 11/1/15.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridViewHolder>{
    private ArrayList<InstagramPost> posts;
    private Context context;

    public PhotoGridAdapter(ArrayList<InstagramPost> posts) {
        this.posts = posts;
    }

    @Override
    public PhotoGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_photo, parent, false);
        return new PhotoGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoGridViewHolder holder, int position) {
        InstagramPost post = posts.get(position);
        holder.sdvPhoto.setAspectRatio(1.0f);
        holder.sdvPhoto.setImageURI(Uri.parse(post.image.imageUrl));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PhotoGridViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvPhoto;

        public PhotoGridViewHolder(View layoutView) {
            super(layoutView);

            sdvPhoto = (SimpleDraweeView) layoutView.findViewById(R.id.sdvPhoto);
        }
    }
}
