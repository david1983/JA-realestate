package uk.co.davideandreazzini.jarealestate;

import android.os.Bundle;

public class AboutActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.content_about);
    }

}
