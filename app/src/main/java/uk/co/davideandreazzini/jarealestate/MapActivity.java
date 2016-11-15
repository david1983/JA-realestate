package uk.co.davideandreazzini.jarealestate;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Models.IntentExtras;
import Models.Property;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapActivity extends BaseActivity {
    GoogleMap map;

    private class propMarker{
        Property prop;
        Marker propMarker;
        public propMarker(Property prop, Marker propMarker) {
            this.prop = prop;
            this.propMarker = propMarker;
        }
    }

    ArrayList<propMarker> arrProp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.content_map);
        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(gmap->{
            map = gmap;
            getProp("BUY");
            getProp("RENT");
        });
    }

    @SuppressLint("NewApi")
    protected void getProp(String type){
        Observable.create(new Observable.OnSubscribe<ArrayList<Property>>(){
            @Override
            public void call(Subscriber<? super ArrayList<Property>> subscriber) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference(type);
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Property> arrProp = new ArrayList<Property>();
                        for(DataSnapshot propSnapshot: dataSnapshot.getChildren()){
                            Property prop = propSnapshot.getValue(Property.class);
                            prop.key = propSnapshot.getKey();
                            prop.type=type;
                            prop.setCoords(prop.parseCoord(propSnapshot.child("lat")), prop.parseCoord(propSnapshot.child("lng")));
                            if(prop.coords!=null && !prop.coords.equals(new LatLng(0,0)))
                                arrProp.add(prop);
                        }
                        subscriber.onNext(arrProp);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.unsubscribe();
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(properties -> {
                if(map==null) return;
                BitmapDescriptor markerType = (type=="BUY") ?
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) :
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                for(Property prop : properties){
                    Marker m = map.addMarker(new MarkerOptions()
                            .position(prop.coords)
                            .title(prop.propertyTypeFullDescription)
                            .icon(markerType));
                    propMarker p = new propMarker(prop, m);
                    arrProp.add(p);
                }
                if(properties.size() ==0) return;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(properties.get(0).coords , 13));
                map.setOnInfoWindowClickListener(marker -> {
                    for(propMarker p: arrProp){
                        if(p.propMarker.equals(marker)){
                            ArrayList<IntentExtras> extras = new ArrayList<IntentExtras>();
                            extras.add(new IntentExtras("property", p.prop.key));
                            extras.add(new IntentExtras("collection", p.prop.type));
                            goTo(new PropertyActivity(), extras);
                        }
                    }
                });
            });

    }
}
