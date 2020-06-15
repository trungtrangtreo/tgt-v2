package ca.TransCanadaTrail.TheGreatTrail.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ca.TransCanadaTrail.TheGreatTrail.R;
import ca.TransCanadaTrail.TheGreatTrail.adapters.ArchiveAdapter;
import ca.TransCanadaTrail.TheGreatTrail.models.Achievement;
import ca.TransCanadaTrail.TheGreatTrail.realmdoas.AchievementsDao;
import ca.TransCanadaTrail.TheGreatTrail.utils.ApplicationData;

public class ArchiveFragment extends Fragment implements GetStartedDialogFragment.GetStartedDialogIF {

    private RecyclerView recycleView;
    private TextView tvTitleTop;

    private ArchiveAdapter archiveAdapter;

    private GetStartedDialogFragment getStartedDialogFragment;
    private GetStartedDialogIF getStartedDialogIF;

    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    private ArrayList<Achievement> achievements;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);

        initView(view);

        getStartedDialogIF = (GetStartedDialogIF) getActivity();

        loadAchievements();

        tvTitleTop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (event.getX() >= (tvTitleTop.getTop() - tvTitleTop.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            setGetStartedDialog();
                            return true;
                        }
                    }
                return false;
            }
        });

        return view;
    }

    private void loadAchievements() {
        achievements = AchievementsDao.getInstance().findAll(getActivity());
    }

    private void initView(View view) {
        recycleView = view.findViewById(R.id.recycleView);
        tvTitleTop = view.findViewById(R.id.tvTitleTop);

        archiveAdapter = new ArchiveAdapter(getContext());
        recycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recycleView.setAdapter(archiveAdapter);
    }

    private void setGetStartedDialog() {
        getStartedDialogFragment = new GetStartedDialogFragment();
        getStartedDialogFragment.setGetStartedDialogIF(this);
        getStartedDialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager());

    }


    @Override
    public void onClickGetStarted() {
        getStartedDialogIF.onClickGetStarted();
    }

    public interface GetStartedDialogIF {
        void onClickGetStarted();
    }
}
