package uk.co.davideandreazzini.jarealestate;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Splash activity shows a splash screen and redirects
 * the user to the Main Activity
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Load the logo image on the ImageView R.id.image
        ImageView iv=(ImageView)findViewById(R.id.image);
        iv.setImageResource(R.drawable.jalogo);
        //after one second start the MainActivity
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}
