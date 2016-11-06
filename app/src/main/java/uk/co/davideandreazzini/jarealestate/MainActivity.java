package uk.co.davideandreazzini.jarealestate;

import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import Models.IntentExtras;

public class MainActivity extends BaseActivity {

    VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.content_main);
        setVideo();
        Button buyBtn = (Button) findViewById(R.id.buyBtn);
        Button rentBtn = (Button) findViewById(R.id.rentBtn);
        buyBtn.setOnClickListener(e->goToProperties("BUY"));
        rentBtn.setOnClickListener(e->goToProperties("RENT"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setVideo();
    }


    public void setVideo(){
        if(mVideoView==null){
            mVideoView  = (VideoView) findViewById(R.id.videoView);
            Uri uri=Uri.parse("android.resource://"+getPackageName()+ "/" + R.raw.video);
            mVideoView.setVideoURI(uri);
            mVideoView.start();
        }
    }

}
