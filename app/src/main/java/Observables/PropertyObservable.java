package Observables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import Helpers.utils;
import Models.Property;
import db.FirebaseH;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by ic3 on 05/11/16.
 */

public class PropertyObservable implements Observable.OnSubscribe<ArrayList<Property>> {
    private FirebaseH db = FirebaseH.getInstance();
    private DatabaseReference dbRef;
    private String collection;
    private Integer offset;
    private long lastKey=0;

    public PropertyObservable(String collection, Integer offset){
        super();
        dbRef = db.mDb.getReference(collection);
        this.offset = offset;
        this.collection = collection;
    }


    @Override
    public void call(Subscriber<? super ArrayList<Property>> subscriber) {
        try {
            if(dbRef==null) {
                throw new Exception("no db reference specified");
            }
            Query m = dbRef.orderByChild("amount");

            m = m.startAt(lastKey);

            m = m.limitToFirst(11);
            m.addValueEventListener(handleValueChanges(subscriber));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ValueEventListener handleValueChanges(Subscriber<? super ArrayList<Property>> subscriber){
        final ArrayList<Property> arrProperty = new ArrayList<Property>();
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer n=0;
                for (DataSnapshot property: dataSnapshot.getChildren()){
                    if(n==0) {
                        n++;
                        continue;
                    }
                    Property prop = Property.fromSnapShot(property, collection);
                    lastKey = prop.amount;
                    arrProperty.add(prop);
                }

                subscriber.onNext(arrProperty);
                subscriber.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
                subscriber.unsubscribe();
            }
        };

        return vel;
    }




}
