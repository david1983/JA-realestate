package Observables;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Objects.Property;
import db.FirebaseH;
import rx.Observable;
import rx.Subscriber;

/**
 * PropertyObservable retrieves a list of properties from Firebase Database
 */

public class PropertyObservable implements Observable.OnSubscribe<ArrayList<Property>> {
    private FirebaseH db = FirebaseH.getInstance();
    private DatabaseReference dbRef;
    private String collection;
    private Integer offset;
    private long lastKey=0;

    public PropertyObservable(String collection, Integer offset){
        super();
        // get a reference to the collection (BUY or RENT)
        dbRef = db.mDb.getReference(collection);
        // set the offset as the offset in the argument if null set a default 11
        this.offset = (offset!=null) ? offset : 11;
        this.collection = collection;
    }

    @Override
    public void call(Subscriber<? super ArrayList<Property>> subscriber) {
        try {
            if(dbRef==null) {
                // if the database reference is null throw an erro
                throw new Exception("no db reference specified");
            }
            // Properties are ordered by price from low to high
            Query m = dbRef
                    .orderByChild("amount")
                    .startAt(lastKey)
                    .limitToFirst(offset);

            m.addValueEventListener(handleValueChanges(subscriber));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * wrapper for firebase value event listener, in this way is possible to pass the Subscriber
     * into the firebase value change event
     *
     * @param subscriber
     * @return
     */
    private ValueEventListener handleValueChanges(Subscriber<? super ArrayList<Property>> subscriber){
        // create the array of properties;
        final ArrayList<Property> arrProperty = new ArrayList<Property>();
        // create the valueEventListener
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // datasnapShot.getChildren() is an array of snapshot
                // containing the properties data as hash-maps
                for (DataSnapshot property: dataSnapshot.getChildren()){
                    // get a property using a static method of the Property class
                    Property prop = Property.fromSnapShot(property, collection);
                    lastKey = prop.amount;
                    arrProperty.add(prop);
                }
                // submit the properties retrieved to the subscriber
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
