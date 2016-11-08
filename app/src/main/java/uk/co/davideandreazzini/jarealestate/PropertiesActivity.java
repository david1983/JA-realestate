package uk.co.davideandreazzini.jarealestate;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import java.util.ArrayList;
import Models.Property;
import Observables.PropertyObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PropertiesActivity extends BaseActivity {
    String propertyType;
    ArrayList<Property> myValues = new ArrayList<>();
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(myValues, this);
    PropertyObservable po;
    RecyclerView myView;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.content_properties);

        Log.d("d", getIntent().getExtras().toString());
        propertyType = getIntent().getStringExtra("propType");
        if(propertyType==null) propertyType="BUY";
        setViewTitle();

        progress = (ProgressBar) findViewById(R.id.propprogress);
        myView =  (RecyclerView)findViewById(R.id.recyclerview);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);

        //Populate the ArrayList with your own values

        po = new PropertyObservable(propertyType, 2);
        loadMore();


    }

    public void loadMore(){
        progress.setVisibility(View.VISIBLE);
        myView.setVisibility(View.GONE);
        rx.Observable.create(po)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(properties -> {
                    myValues.addAll(properties);
                    adapter.notifyDataSetChanged();
                    myView.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                });
    }

    private void setViewTitle(){
        String title = (propertyType.equals("BUY")) ? "Buy properties" : "Rent properties";
        setTitle(title);
    }



}
