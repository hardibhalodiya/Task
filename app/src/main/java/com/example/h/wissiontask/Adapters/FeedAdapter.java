package com.example.h.wissiontask.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.h.wissiontask.Models.Feed;
import com.example.h.wissiontask.R;
import com.example.h.wissiontask.YoutubePlayerActivity;

import java.util.List;

/**
 * Created by hardi on 29/06/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    Context context;
    List<Feed> feedList;

    public FeedAdapter(Context context, List<Feed> feedList) {
        this.context = context;
        this.feedList = feedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.youtube_video_custom_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Feed feed = feedList.get(holder.getAdapterPosition());
        holder.video_title_label.setText(feed.getTitle());
        holder.likes.setText(feed.getLikeCount());

        Glide.with(context)
                .load(feed.getThumbnail())
                .into(holder.feed_image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YoutubePlayerActivity.class);
                intent.putExtra("title",feed.getTitle());
                intent.putExtra("description",feed.getDescription());
                intent.putExtra("likes",feed.getLikeCount());
                intent.putExtra("comments",feed.getViewCount());
                intent.putExtra("video_id", feed.getVideoId());
                context.startActivity(intent);

            }
        });

        holder.likes_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(holder.likes_btn.getVisibility() == View.VISIBLE){
                    int count = Integer.parseInt(String.valueOf(holder.likes.getText()));
                    holder.likes.setText(String.valueOf(++count));
                    holder.likes_btn.setVisibility(View.GONE);
                    holder.likes_fill_btn.setVisibility(View.VISIBLE);
                }else{
                    int count = Integer.parseInt(String.valueOf(holder.likes.getText()));
                    holder.likes.setText(String.valueOf(--count));
                    holder.likes_btn.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.likes_fill_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(holder.likes_fill_btn.getVisibility() == View.VISIBLE){
                    int count = Integer.parseInt(String.valueOf(holder.likes.getText()));
                    holder.likes.setText(String.valueOf(--count));
                    holder.likes_fill_btn.setVisibility(View.GONE);
                    holder.likes_btn.setVisibility(View.VISIBLE);
                }else{
                    holder.likes_fill_btn.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(String.valueOf(holder.likes.getText()));
                    holder.likes.setText(String.valueOf(++count));
                }
            }
        });
        holder.comments_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if(holder.comments_btn.getVisibility() == View.VISIBLE){
                    holder.comments_btn.setVisibility(View.GONE);
                    holder.comments_fill_btn.setVisibility(View.VISIBLE);
                    holder.post_txt_layout.setVisibility(View.VISIBLE);
                    holder.postbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String poastData = holder.comments_edittext.getText().toString();
                            holder.post_txt_layout.setVisibility(View.GONE);
                            holder.postDetail.setVisibility(View.VISIBLE);
                            holder.postDetail.setText(poastData);
                        }
                    });
                }else{
                    holder.comments_btn.setVisibility(View.VISIBLE);
                    holder.post_txt_layout.setVisibility(View.GONE);
                }
            }
        });
        holder.comments_fill_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if(holder.comments_fill_btn.getVisibility() == View.VISIBLE){
                    holder.comments_fill_btn.setVisibility(View.GONE);
                    holder.comments_btn.setVisibility(View.VISIBLE);
                    holder.post_txt_layout.setVisibility(View.GONE);
                }else{
                    holder.comments_fill_btn.setVisibility(View.VISIBLE);
                    holder.post_txt_layout.setVisibility(View.VISIBLE);
                    holder.postbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String poastData = holder.comments_edittext.getText().toString();
                            holder.post_txt_layout.setVisibility(View.GONE);
                            holder.postDetail.setVisibility(View.VISIBLE);
                            holder.postDetail.setText(poastData);
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView feed_image;
        private TextView video_title_label, likes, comments,postDetail;
        private ImageButton likes_btn, comments_btn;
        private ImageButton likes_fill_btn, comments_fill_btn;
        private CardView cardView;
        private LinearLayout post_txt_layout;
        private EditText comments_edittext;
        private Button postbutton;

        public MyViewHolder(View itemView) {
            super(itemView);

            feed_image = (ImageView) itemView.findViewById(R.id.feed_image);
            video_title_label = (TextView) itemView.findViewById(R.id.video_title_label);
            likes = (TextView) itemView.findViewById(R.id.likes);
            comments = (TextView) itemView.findViewById(R.id.comments);
            likes_btn = (ImageButton) itemView.findViewById(R.id.likes_btn);
            comments_btn = (ImageButton) itemView.findViewById(R.id.comments_btn);
            likes_fill_btn = (ImageButton) itemView.findViewById(R.id.likes_fill_btn);
            comments_fill_btn = (ImageButton) itemView.findViewById(R.id.comments_fill_btn);
            cardView = (CardView) itemView.findViewById(R.id.complete_card);
            post_txt_layout = (LinearLayout) itemView.findViewById(R.id.post_txt_layout);
            comments_edittext = (EditText) itemView.findViewById(R.id.comments_edittext);
            postbutton = (Button) itemView.findViewById(R.id.postbutton);
            postDetail = (TextView) itemView.findViewById(R.id.postDetail);
        }
    }
}
