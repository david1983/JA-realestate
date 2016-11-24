package uk.co.davideandreazzini.jarealestate;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;

import Objects.IntentExtras;
import db.FirebaseH;

/**
 * Created by david on 09/10/2016.
 */

public class BaseActivity extends AppCompatActivity {

    ArrayList<IntentExtras> intentExtras = new ArrayList<>();
    FirebaseH db = FirebaseH.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        db.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };

    }

    /**
     * setToolbar is used to set the back arrow on the toolbar
     */
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
        // handle back arrow click
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * goTo is used to provide a basic navigation through the different activities
     * @param a
     * @param values
     */
    public void goTo(Activity a, ArrayList<IntentExtras> values){
        //Create a new Intenr using the activity passed in the arguments
        Intent intent = new Intent(getApplicationContext(), a.getClass());
        //Generate intent extras from the list of intents in the arguments
        if(values!=null){
            for(IntentExtras i: values){
                intent.putExtra(i.name, i.value);
            }
        }
        //Start the intent
        startActivity(intent);
    }

    /**
     * goToProperties is a shortcut for redirect the user to Buy/Rent properties Activity
     * @param type
     */
    public void goToProperties(String type){

        intentExtras.clear();
        intentExtras.add(new IntentExtras("propType", type));
        goTo(new PropertiesActivity(),intentExtras);
    }


    // Handle auth state listener using Activity lifecycle's callbacks to avoid memory leaks
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
