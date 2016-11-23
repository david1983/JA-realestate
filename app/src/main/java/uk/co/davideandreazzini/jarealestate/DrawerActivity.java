package uk.co.davideandreazzini.jarealestate;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Helpers.utils;
import Models.IntentExtras;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<IntentExtras> intentExtras = new ArrayList<>();
    Boolean drawerOn;
    Menu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mMenu = navigationView.getMenu();
        user = db.mAuth.getCurrentUser();
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                setUserDrawer(user);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                setUserDrawer(user);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                setUserDrawer(user);
            }
        });


    }


    public void setView(int layout_id){
        LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = inflater.inflate(layout_id,
                (ViewGroup) findViewById(R.id.include_view));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setUserDrawer(FirebaseUser user){
        if(findViewById(R.id.userImage)==null)return;
        ImageView userImg = (ImageView) findViewById(R.id.userImage);
        TextView username = (TextView) findViewById(R.id.userName);
        TextView useremail = (TextView) findViewById(R.id.userEmail);

        MenuItem login = mMenu.findItem(R.id.nav_login);
        MenuItem logout = mMenu.findItem(R.id.nav_logout);

        if(user==null) {
            if(logout!=null)logout.setVisible(false);
            if(login!=null)login.setVisible(true);

            userImg.setVisibility(View.GONE);
            username.setVisibility(View.GONE);
            useremail.setVisibility(View.GONE);
            return;
        }else{
            if(logout!=null)logout.setVisible(true);
            if(login!=null)login.setVisible(false);
            userImg.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            useremail.setVisibility(View.VISIBLE);
            Uri photoUrl = user.getPhotoUrl();

            username.setText(user.getDisplayName());

            useremail.setText(user.getEmail());
            if(photoUrl != null){
                userImg.setImageBitmap(utils.loadBitmap(photoUrl.toString()));
            }
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToProperties(String type){
        intentExtras.clear();
        intentExtras.add(new IntentExtras("propType", type));
        goTo(new PropertiesActivity(),intentExtras);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        intentExtras.clear();

        if (id == R.id.nav_buy) {
            goToProperties("BUY");
        } else if (id == R.id.nav_rent) {
            goToProperties("RENT");
        } else if (id == R.id.nav_sell) {
            goTo(new PropertySellActivity(), null);
        } else if (id == R.id.nav_map) {
            goTo(new MapActivity(),null);
        } else if (id == R.id.nav_home) {
            goTo(new MainActivity(),null);
        } else if (id == R.id.nav_login) {
            goTo(new LoginActivity(), null);
        } else if(id==R.id.nav_logout){
            db.mAuth.signOut();
            user = null;
            setUserDrawer(null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        user = db.mAuth.getCurrentUser();
        setUserDrawer(user);
    }

    @Override
    public void onResume() {
        super.onResume();
        user = db.mAuth.getCurrentUser();
        setUserDrawer(user);
    }
}
