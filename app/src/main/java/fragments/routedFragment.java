package fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import db.FirebaseH;
import uk.co.davideandreazzini.jarealestate.R;


/**
 * Created by david on 05/10/2016.
 */

public class routedFragment extends Fragment {
    protected FirebaseH db = FirebaseH.getInstance();

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void goTo(Fragment fragment, Boolean useBackStack){
        FragmentTransaction fragMan = getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.enter_left, R.animator.exit_left,
                        R.animator.exit_right, R.animator.enter_right)
                .replace(uk.co.davideandreazzini.jarealestate.R.id.fragmentView, fragment);
                if(useBackStack) fragMan.addToBackStack(null);
                fragMan.commit();
    }

}
