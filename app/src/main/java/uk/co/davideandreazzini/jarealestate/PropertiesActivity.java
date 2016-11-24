package uk.co.davideandreazzini.jarealestate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import java.util.ArrayList;
import Objects.Property;
import Observables.PropertyObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PropertiesActivity extends DrawerActivity {
    String propertyType;
    ArrayList<Property> propertyList = new ArrayList<>();
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(propertyList, this);
    PropertyObservable po;
    RecyclerView myView;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.content_properties);

        // get intent extras that define what properies to show
        propertyType = getIntent().getStringExtra("propType");
        if(propertyType==null) propertyType="BUY";
        setViewTitle();

        progress = (ProgressBar) findViewById(R.id.propprogress);
        
        // A RecyclerView is used to show the properties
        myView =  (RecyclerView)findViewById(R.id.recyclerview);
        myView.setAdapter(adapter);
        
        // if the orientation is landscape a grid with 3 columns is shown
        // otherwise 2 columns grid is show
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            myView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        else{
            myView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        }

        po = new PropertyObservable(propertyType, 11);
        loadMore();
    }

    /**
     * loadMore is the method responsible of loading the properties into the Property list
     * 
     */
    public void loadMore(){
        progress.setVisibility(View.VISIBLE);
        myView.setVisibility(View.GONE);
        // Using rxJava an observer can submit to the PropertyObservable on a separate thread
        // freeing the UI thread of expensive calculations
        rx.Observable.create(po)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(properties -> {
                    // the onNext return an ArrayList of Properties
                    // that is then pushed into the propertyList
                    propertyList.addAll(properties);
                    // the adapter need to be notified of the changes in
                    // order to update the UI
                    adapter.notifyDataSetChanged();
                    myView.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }, error -> {
                    Log.e("ERROR", error.getMessage());
                });
    }

    /**
     * setViewTitle set the title shown into the navigation bar
     */
    private void setViewTitle(){
        String title = (propertyType.equals("BUY")) ? "Buy properties" : "Rent properties";
        setTitle(title);
    }

}
