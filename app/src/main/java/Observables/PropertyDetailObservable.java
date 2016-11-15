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
 * PropertyDetailObservable is used to get the details of a property
 */

public class PropertyDetailObservable implements Observable.OnSubscribe<Property> {
    private FirebaseH db = FirebaseH.getInstance();
    private DatabaseReference dbRef;
    private String collection;

    public PropertyDetailObservable(String collection, String key) {
        this.collection = collection;
        //Set the database reference to the given key
        dbRef = db.mDb.getReference(collection).child(key);
    }


    @Override
    public void call(Subscriber<? super Property> subscriber) {

        // Add a valueEvent listener when the Observer subscrive
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /**
                 * when the data change (Async) generate a Property object from the Firebase snapShot
                 * and return it to the onNext call of the subscriber, after that terminate the subscriber connection.
                 */

                Property prop = Property.fromSnapShot(dataSnapshot, collection);
                subscriber.onNext(prop);
                subscriber.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Terminate the subscriber connection in case of databaseError in order to avoid memory leaks
                subscriber.unsubscribe();
            }
        });
    }
}
