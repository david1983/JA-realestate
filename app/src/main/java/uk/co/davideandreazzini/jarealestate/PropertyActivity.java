package uk.co.davideandreazzini.jarealestate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.Observable;

import Observables.PropertyDetailObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PropertyActivity extends FirebaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        setToolbar();
        String propKey = getIntent().getStringExtra("property");
        String collection = getIntent().getStringExtra("collection");
        PropertyDetailObservable pdo = new PropertyDetailObservable(collection,propKey);
        rx.Observable.create(pdo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(property->{
                    Log.d("d", property.toString());
                });
    }

}
