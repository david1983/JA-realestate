package Observables;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import Models.Property;
import db.FirebaseH;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by ic3 on 06/11/16.
 */

public class PropertyDetailObservable implements Observable.OnSubscribe<Property> {
    private FirebaseH db = FirebaseH.getInstance();
    private DatabaseReference dbRef;
    private String collection;

    public PropertyDetailObservable(String collection, String key) {
        this.collection = collection;
        dbRef = db.mDb.getReference(collection).child(key);
    }


    @Override
    public void call(Subscriber<? super Property> subscriber) {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Property prop = Property.fromSnapShot(dataSnapshot, collection);
                subscriber.onNext(prop);
                subscriber.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
                subscriber.unsubscribe();
            }
        });
    }
}
