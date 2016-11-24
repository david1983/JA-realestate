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

        //defining buttons from the view
        Button buyBtn = (Button) findViewById(R.id.buyBtn);
        Button rentBtn = (Button) findViewById(R.id.rentBtn);
        Button submitBtn = (Button) findViewById(R.id.submitBtn);

        // Java 8 lambda function is used to handle onclicklistener
        buyBtn.setOnClickListener(e->goToProperties("BUY"));
        rentBtn.setOnClickListener(e->goToProperties("RENT"));
        submitBtn.setOnClickListener(e->goTo(new SubmitPropertyActivity(),null));
    }

    /**
     * onConfigurationChanged is needed to resume the video when
     * orientation changes
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setVideo();
    }

    /**
     * setVideo method is used to setup the VideoView element.
     * android:configChanges="orientation|screenSize" was needed in the manifest file
     * in order to allow the resume of the video when the device changes orientation
     */
    private void setVideo(){
        // if the video is already running don't do anything
        if(mVideoView==null){
            mVideoView  = (VideoView) findViewById(R.id.videoView);
            // get the video resource from the raw folder
            Uri uri=Uri.parse("android.resource://"+getPackageName()+ "/" + R.raw.video);
            mVideoView.setVideoURI(uri);
            mVideoView.setOnPreparedListener(mp->{
                // set volume to 0 and loop the video
                mp.setVolume(0,0);
                mp.setLooping(true);
            });
            mVideoView.start();
        }
    }

}
