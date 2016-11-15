package Observables;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Models.Property;
import db.FirebaseH;
import rx.Observable;
import rx.Subscriber;

/**
 * PropertyObservable retrieves a list of properties
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
                for (DataSnapshot property: dataSnapshot.getChildren()){
                    Property prop = Property.fromSnapShot(property, collection);
                    lastKey = prop.amount;
                    arrProperty.add(prop);
                }

                subscriber.onNext(arrProperty);
                subscriber.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Terminate the subscriber connection in case of databaseError in order to avoid memory leaks
                subscriber.unsubscribe();
            }
        };

        return vel;
    }




}
