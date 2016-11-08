package uk.co.davideandreazzini.jarealestate;

import android.os.Bundle;

import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import Models.Property;
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
                    setPropertyUI(property);
                });
    }

    protected void setPropertyUI(Property prop){
        TextView title = (TextView) findViewById(R.id.PropertyTitle);
        TextView summary = (TextView) findViewById(R.id.PropertySummary);
        TextView address = (TextView) findViewById(R.id.PropertyAddress);
        TextView bedrooms = (TextView) findViewById(R.id.PropertyBedrooms);
        TextView price = (TextView) findViewById(R.id.PropertyPrice);
        TextView type = (TextView) findViewById(R.id.PropertyType);
        ImageView img = (ImageView) findViewById(R.id.PropertyMainImg);

        img.setImageBitmap(prop.bmp);

        title.setText(prop.propertyTypeFullDescription);
        summary.setText( prop.summary);
        address.setText("Address: " + prop.displayAddress);
        price.setText("Price: " + prop.displayAmount);
        bedrooms.setText("N. of bedrooms:" + prop.bedrooms);
        type.setText("Type: "+ prop.type);
    }

}
