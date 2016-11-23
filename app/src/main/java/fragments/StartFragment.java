package fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;

import uk.co.davideandreazzini.jarealestate.LoginActivity;
import uk.co.davideandreazzini.jarealestate.R;

/**
 * Created by david on 04/10/2016.
 */

public class StartFragment extends routedFragment {

    Button loginBtn;
    Button registerBtn;
    Button googleBtn;
    CallbackManager mCallbackManager;
    static private String TAG = "INFO";


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(uk.co.davideandreazzini.jarealestate.R.layout.fragment_start, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginBtn = (Button) getView().findViewById(uk.co.davideandreazzini.jarealestate.R.id.loginBtn);
        registerBtn = (Button) getView().findViewById(uk.co.davideandreazzini.jarealestate.R.id.registerBtn);
        googleBtn = (Button) getView().findViewById(uk.co.davideandreazzini.jarealestate.R.id.googleLogin);
        loginBtn.setOnClickListener( e-> goTo(new LoginFragment(), true));
        registerBtn.setOnClickListener(e->{
            LoginFragment registerFrag = new LoginFragment();
            registerFrag.state=1;
            goTo(registerFrag, true);
        });
        googleBtn.setOnClickListener(e->googleLoginFn());

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) getView().findViewById(R.id.facebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                getActivity().finish();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(getActivity(), "Authentication failed. " + error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void googleLoginFn(){
        Activity a = getActivity();
        if(a instanceof LoginActivity){
            ((LoginActivity) a).signIn();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        db.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


}
