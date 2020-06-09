package ca.TransCanadaTrail.TheGreatTrail.howtouse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import ca.TransCanadaTrail.TheGreatTrail.R;

public class HowToUseActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private TabLayout tabLayout;
    private PhotosPagerAdapter photosPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        initView();
        initAdapter();
    }

    private void initView() {
        viewpager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void initAdapter() {
        photosPagerAdapter = new PhotosPagerAdapter(this);
        viewpager.setAdapter(photosPagerAdapter);

        tabLayout.setupWithViewPager(viewpager, true);
    }
}
