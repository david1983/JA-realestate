package uk.co.davideandreazzini.jarealestate;

import android.os.Bundle;

import fragments.LoginFragment;
import fragments.StartFragment;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.content_login);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentView, new StartFragment())
                    .commit();
        }
    }

}
