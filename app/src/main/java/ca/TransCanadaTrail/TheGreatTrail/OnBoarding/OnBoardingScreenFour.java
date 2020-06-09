package ca.TransCanadaTrail.TheGreatTrail.OnBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.TransCanadaTrail.TheGreatTrail.R;

public class OnBoardingScreenFour extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.onboardingscr4,container,false);
        return v;
    }
}
