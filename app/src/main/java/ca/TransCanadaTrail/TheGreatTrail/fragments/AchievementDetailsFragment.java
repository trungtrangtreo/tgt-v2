package ca.TransCanadaTrail.TheGreatTrail.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import ca.TransCanadaTrail.TheGreatTrail.R;

public class AchievementDetailsFragment extends Fragment {
    private static final String ARGUMENT_ACHIEVEMENT_ID = "args-achievement-id";

    @BindView(R.id.achievement_title_tv)
    TextView achievementTitleTV;

    @BindView(R.id.achievement_iv)
    ImageView achievementIV;

    @BindView(R.id.achievement_description_tv)
    TextView achievementDescriptionTV;

    @BindView(R.id.earned_date_tv)
    TextView earnedDateTV;

    @BindString(R.string.achievement_earned_date_format)
    String dateFormat;

    public static AchievementDetailsFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_ACHIEVEMENT_ID, id);

        AchievementDetailsFragment fragment = new AchievementDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement_details, container, false);
        ButterKnife.bind(this, view);
        setUIValues();

        return view;
    }

    private void setUIValues() {
        Bundle args = getArguments();
        int id = args.getInt(ARGUMENT_ACHIEVEMENT_ID);
        switch (id){
            case 1:
                achievementTitleTV.setText("Download");
                achievementDescriptionTV.setText("Thanks for downloading app!\nExplore Canada via The Great Trail");
                achievementIV.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_download_archive_yellow));
                break;
            case 2:
                achievementTitleTV.setText("Navigation");
                achievementDescriptionTV.setText("Search the app to find the Trail nearest \n you and get out on the Great Trail.");
                achievementIV.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_navigation));
                break;
            case 3:
                achievementTitleTV.setText("Experience");
                achievementDescriptionTV.setText("Spend 2 hours exploring on The Great Trail");
                achievementIV.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_experience));
                break;
            case 4:
                achievementTitleTV.setText("Explorer");
                achievementDescriptionTV.setText("Explore the Trail in 2 Provinces");
                achievementIV.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_explorer));
                break;
            case 5:
                achievementTitleTV.setText("Adventure");
                achievementDescriptionTV.setText("Visit the Trail 2x in a week.  Use the Trail to commute or plan a weekend adventure.");
                achievementIV.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_adventure));
                break;
            case 6:
                achievementTitleTV.setText("Tracker");
                achievementDescriptionTV.setText("Get active on the Great Trail, track 10km on the Trail.");
                achievementIV.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_tracker));
                break;
            case 7:
                achievementTitleTV.setText("Champion");
                achievementDescriptionTV.setText("Get active on the Great Trail, track 50km on the Trail.");
                achievementIV.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_champion));
                break;
            case 8:
                achievementTitleTV.setText("Elevation");
                achievementDescriptionTV.setText("Change of elevation. Enjoy the views\nalong The Great Trail, gain 150 meters.");
                achievementIV.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_elevation));
                break;
        }
    }
}