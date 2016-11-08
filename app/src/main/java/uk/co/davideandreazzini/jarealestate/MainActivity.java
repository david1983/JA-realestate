package uk.co.davideandreazzini.jarealestate;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;

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
            mVideoView.setOnPreparedListener(mp->{
                mp.setVolume(0,0);
            });
            mVideoView.start();
        }
    }

}
