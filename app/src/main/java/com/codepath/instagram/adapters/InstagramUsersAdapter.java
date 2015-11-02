package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by mrucker on 10/31/15.
 */
public class InstagramUsersAdapter extends RecyclerView.Adapter<InstagramUsersAdapter.UsersViewHolder> {
    private ArrayList<InstagramUser> users;
    private Context context;

    public InstagramUsersAdapter(ArrayList<InstagramUser> users) {
        this.users = users;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_user, parent, false);
        UsersViewHolder viewHolder = new UsersViewHolder(view, new UsersViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, PhotoGridActivity.class);
                intent.putExtra("keyword", users.get(position).userId);
                intent.putExtra("photoType", "user");
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        InstagramUser user = users.get(position);

        holder.tvSearchFullName.setText(user.fullName);

        holder.tvSearchUserName.setText(user.userName);

        holder.sdvUserProfilePicture.setAspectRatio(1.0f);
        holder.sdvUserProfilePicture.setImageURI(Uri.parse(user.profilePictureUrl));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvSearchUserName;
        public TextView tvSearchFullName;
        public SimpleDraweeView sdvUserProfilePicture;
        public OnItemClickListener clickListener;

        public UsersViewHolder(View layoutView, OnItemClickListener listener) {
            super(layoutView);

            clickListener = listener;
            layoutView.setOnClickListener(this);
            tvSearchUserName = (TextView) layoutView.findViewById(R.id.tvSearchUserName);
            tvSearchFullName = (TextView) layoutView.findViewById(R.id.tvSearchFullName);
            sdvUserProfilePicture = (SimpleDraweeView) layoutView.findViewById(R.id.sdvUserProfilePicture);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }
    }
}
