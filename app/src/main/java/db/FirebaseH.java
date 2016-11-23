package db;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * FirebaseH is the Firebase Database Handler
 * is a singleton object so it can only be instanced once.
 * the instance is shared across the application avoiding the generation
 * of new objects and pollution of the garbage collector
 *
 */
public class FirebaseH {
    public FirebaseDatabase mDb;
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;

    private static FirebaseH ourInstance = new FirebaseH();

    public static FirebaseH getInstance() {
        return ourInstance;
    }

    private FirebaseH() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDb = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
}
