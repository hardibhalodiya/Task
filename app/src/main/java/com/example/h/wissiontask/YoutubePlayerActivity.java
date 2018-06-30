package com.example.h.wissiontask;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.concurrent.TimeoutException;


/**
 * Created by hardi on 29/06/18.
 */

public class YoutubePlayerActivity extends YouTubeBaseActivity {
    private static final String TAG = YoutubePlayerActivity.class.getSimpleName();
    private String videoID;
    private YouTubePlayerView youTubePlayerView;
    private ImageButton likes_btn, comments_btn;
    private ImageButton likes_fill_btn, comments_fill_btn;
    private LinearLayout post_txt_layout;
    private TextView video_title_label, video_description_label, likes, comments,postDetail;
    private EditText comments_edittext;
    private Button postbutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_player_activity);

        videoID = getIntent().getStringExtra("video_id");
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        video_title_label = (TextView) findViewById(R.id.video_title_label);
        video_description_label = (TextView) findViewById(R.id.video_description_label);
        video_title_label.setText(getIntent().getStringExtra("title"));
        video_description_label.setText(getIntent().getStringExtra("description"));

        initializeYoutubePlayer();
    }


    private void initializeYoutubePlayer() {
        youTubePlayerView.initialize(MyApplication.getInstance().getDeveloperKey(), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

                if (!wasRestored) {
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.loadVideo(videoID);

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }

}
