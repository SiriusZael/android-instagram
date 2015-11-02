package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;

import java.util.ArrayList;

/**
 * Created by mrucker on 11/1/15.
 */
public class InstagramTagsAdapter extends RecyclerView.Adapter<InstagramTagsAdapter.TagsViewHolder>{
    private ArrayList<InstagramSearchTag> tags;
    private Context context;

    public InstagramTagsAdapter(ArrayList<InstagramSearchTag> tags) {
        this.tags = tags;
    }

    @Override
    public TagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_tag, parent, false);
        TagsViewHolder viewHolder = new TagsViewHolder(view, new TagsViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, PhotoGridActivity.class);
                intent.putExtra("keyword", tags.get(position).tag);
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TagsViewHolder holder, int position) {
        InstagramSearchTag tag = tags.get(position);

        Resources res = context.getResources();
        String postCount = res.getQuantityString(R.plurals.like_messages, tag.count, Utils.formatNumberForDisplay(tag.count));

        holder.tvSearchTagName.setText(res.getString(R.string.hashtag, tag.tag));
        holder.tvSearchTagPosts.setText(String.valueOf(postCount));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class TagsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvSearchTagName;
        public TextView tvSearchTagPosts;
        private OnItemClickListener clickListener;

        public TagsViewHolder(View layoutView, OnItemClickListener listener) {
            super(layoutView);
            clickListener = listener;
            tvSearchTagName = (TextView) layoutView.findViewById(R.id.tvSearchTagName);
            tvSearchTagPosts = (TextView) layoutView.findViewById(R.id.tvSearchTagPosts);
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
