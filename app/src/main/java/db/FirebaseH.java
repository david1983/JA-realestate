package db;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ic3 on 06/11/16.
 */
public class FirebaseH {
    public FirebaseDatabase mDb;
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseUser user;

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
