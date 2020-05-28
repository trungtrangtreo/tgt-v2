package ca.TransCanadaTrail.TheGreatTrail.howtouse;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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
