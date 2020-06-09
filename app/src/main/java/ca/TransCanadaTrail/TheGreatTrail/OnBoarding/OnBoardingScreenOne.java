package ca.TransCanadaTrail.TheGreatTrail.OnBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import ca.TransCanadaTrail.TheGreatTrail.R;

public class OnBoardingScreenOne extends Fragment {
    ImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.onboardingscr1,container,false);
        image=(ImageView)v.findViewById(R.id.imageView2) ;
        if(Locale.getDefault().getLanguage().equals("fr")){
            image.setImageResource(R.drawable.yellow_slide1_fr);
        }

        return v;
    }
}
