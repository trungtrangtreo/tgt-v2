package ca.TransCanadaTrail.TheGreatTrail.OnBoarding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.TransCanadaTrail.TheGreatTrail.R;

/**
 * Created by hardikfumakiya on 2017-01-05.
 */

public class OnBoardingScreenFive extends Fragment {
    public static OnBoardingScreenFive instance = null;
    public static OnBoardingScreenFive getInstance() {
       // if (instance == null) {
            instance = new OnBoardingScreenFive();
        //}
        Log.i("Instance AT", "No Creation");
        return instance;
    }


    public static OnBoardingScreenFive newInstance() {
        OnBoardingScreenFive fragment = getInstance();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.onboardingscr5,container,false);

        return v;
    }
}
