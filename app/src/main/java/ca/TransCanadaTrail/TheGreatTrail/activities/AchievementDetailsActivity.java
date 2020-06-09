package ca.TransCanadaTrail.TheGreatTrail.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import ca.TransCanadaTrail.TheGreatTrail.R;
import ca.TransCanadaTrail.TheGreatTrail.fragments.AchievementDetailsFragment;

public class AchievementDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ACHIEVEMENT_ID = "extra-achievement-id";

    @BindView(R.id.acheievement_details_activity_tb)
    Toolbar toolbar;

    @BindArray(R.array.achievement_icons)
    TypedArray achievementIcons;

    private int id;

    public static Intent newIntent(Context context, int id) {
        Intent intent = new Intent(context, AchievementDetailsActivity.class);
        intent.putExtra(EXTRA_ACHIEVEMENT_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_details);
        ButterKnife.bind(this);

        id = getIntent().getIntExtra(EXTRA_ACHIEVEMENT_ID, -1);

        setActionBar();
        setFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = AchievementDetailsFragment.newInstance(id);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
//      Achievement achievement = AchievementsDao.getInstance().findWithId(this, Achievement.ID_FIELD_NAME, id).get(0);
//      getSupportActionBar().setTitle(achievement.getAchievementTitle(this));
    }

}
