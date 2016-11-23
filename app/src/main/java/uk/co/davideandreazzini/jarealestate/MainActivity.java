package uk.co.davideandreazzini.jarealestate;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends DrawerActivity {

    VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.content_main);
        setVideo();
        Button buyBtn = (Button) findViewById(R.id.buyBtn);
        Button rentBtn = (Button) findViewById(R.id.rentBtn);
        Button submitBtn = (Button) findViewById(R.id.submitBtn);
        buyBtn.setOnClickListener(e->goToProperties("BUY"));
        rentBtn.setOnClickListener(e->goToProperties("RENT"));
        submitBtn.setOnClickListener(e->goTo(new PropertySellActivity(),null));
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
                mp.setLooping(true);
            });
            mVideoView.start();
        }
    }

}
