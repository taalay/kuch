package com.tali.admin.kuch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tali.admin.kuch.R;
import com.tali.admin.kuch.model.Post;

import java.io.File;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
    private static final String TAG = RecyclerviewAdapter.class.getSimpleName();
    private Context mContext;
    OnItemClickListener mItemClickListener;
    private List<Post> posts;

    // 2
    public RecyclerviewAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.posts = posts;
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView authorImg;
        public TextView authorName;
        public TextView themeDate;
        public ExpandableTextView description;
        public ImageView themeImg;
        public TextView location;

        public ViewHolder(View itemView) {
            super(itemView);
            authorImg = (ImageView) itemView.findViewById(R.id.t_image);
            authorName = (TextView) itemView.findViewById(R.id.t_authorName);
            themeDate = (TextView) itemView.findViewById(R.id.theme_date);
            themeImg = (ImageView) itemView.findViewById(R.id.t_theme_img);
            description = initExpandDescription(itemView);
            location = (TextView) itemView.findViewById(R.id.t_location);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }

        private ExpandableTextView initExpandDescription(View v) {
            final ExpandableTextView expandableTextView = (ExpandableTextView) v.findViewById(R.id.expandableTextView);
            final TextView buttonToggle = (TextView) v.findViewById(R.id.button_toggle);
            expandableTextView.setAnimationDuration(1000L);
            expandableTextView.setExpandInterpolator(new OvershootInterpolator());
            expandableTextView.setCollapseInterpolator(new OvershootInterpolator());
            buttonToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    expandableTextView.toggle();
                    buttonToggle.setText(expandableTextView.isExpanded() ? R.string.collapse : R.string.expand);
                }
            });

            buttonToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (expandableTextView.isExpanded()) {
                        expandableTextView.collapse();
                        buttonToggle.setText(R.string.expand);
                    } else {
                        expandableTextView.expand();
                        buttonToggle.setText(R.string.collapse);
                    }
                }
            });

            expandableTextView.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
                @Override
                public void onExpand(final ExpandableTextView view) {
                    Log.d(TAG, "ExpandableTextView expanded");
                }

                @Override
                public void onCollapse(final ExpandableTextView view) {
                    Log.d(TAG, "ExpandableTextView collapsed");
                }
            });
            return expandableTextView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    // 1
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // 2
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_theme, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Post post = posts.get(position);
        Picasso.with(mContext).load(post.getUser().getProfilePictureUrl())
                .placeholder(R.drawable.no_photo)
                .error(R.drawable.no_photo)
                .into(holder.authorImg, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                .load(new File(post.getUser().getProfilePictureUrl()))
                                .placeholder(R.drawable.no_photo)
                                .error(R.drawable.no_photo)
                                .into(holder.authorImg);
                    }
                });
        holder.authorName.setText(post.getUser().getUserName());
        holder.themeDate.setText(post.getThemeDate());
        holder.description.setText(post.getDescription());
        holder.location.setText(post.getLocation());
        if (!post.getThemeImg().equals("")) {
            holder.themeImg.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(post.getThemeImg())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.themeImg, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(mContext)
                                    .load(new File(post.getThemeImg()))
                                    .placeholder(R.drawable.no_image)
                                    .error(R.drawable.no_image)
                                    .into(holder.themeImg);
                        }
                    });
        }
    }

    /*public void clear() {
        for (Theme theme : posts) {
            Picasso.with(mContext).invalidate(new File(theme.getThemeImg()));
            Picasso.with(mContext).invalidate(new File(theme.getAuthorImg()));
        }
    }*/
}
