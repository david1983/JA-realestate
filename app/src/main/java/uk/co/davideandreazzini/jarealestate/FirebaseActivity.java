package uk.co.davideandreazzini.jarealestate;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;

import Models.IntentExtras;
import db.FirebaseH;

/**
 * Created by david on 09/10/2016.
 */

public class FirebaseActivity extends AppCompatActivity {

    FirebaseH db = FirebaseH.getInstance();
    FirebaseUser user = db.user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void goTo(Activity a, ArrayList<IntentExtras> values){

        Intent intent = new Intent(getApplicationContext(), a.getClass());
        if(values!=null){
            for(IntentExtras i: values){
                intent.putExtra(i.name, i.value);
            }
        }

        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        db.mAuth.addAuthStateListener(db.mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db.mAuthListener != null) {
            db.mAuth.removeAuthStateListener(db.mAuthListener);
        }
    }

}
