package ca.TransCanadaTrail.TheGreatTrail.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.TransCanadaTrail.TheGreatTrail.MainActivity;
import ca.TransCanadaTrail.TheGreatTrail.R;
import ca.TransCanadaTrail.TheGreatTrail.adapters.ArchiveAdapter;
import ca.TransCanadaTrail.TheGreatTrail.utils.ApplicationData;

public class ArchiveFragment extends Fragment implements GetStartedDialogFragment.GetStartedDialogIF {

    private RecyclerView recycleView;

    private ArchiveAdapter archiveAdapter;

    private GetStartedDialogFragment getStartedDialogFragment;
    private GetStartedDialogIF getStartedDialogIF;

    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);

        initView(view);

        getStartedDialogIF = (GetStartedDialogIF) getActivity();

        setGetStartedDialog();

        return view;
    }

    private void initView(View view) {
        recycleView = view.findViewById(R.id.recycleView);

        archiveAdapter =new ArchiveAdapter(getContext());
        recycleView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recycleView.setAdapter(archiveAdapter);
    }

    private void setGetStartedDialog() {
        getStartedDialogFragment = new GetStartedDialogFragment();
        getStartedDialogFragment.setGetStartedDialogIF(this);
        ApplicationData applicationData = ApplicationData.getInstance(getActivity());
        if (applicationData.showGetStartedDialog()) {
            getStartedDialogFragment.show(getActivity().getSupportFragmentManager());
        }
    }

    @Override
    public void onClickGetStarted() {
        getStartedDialogIF.onClickGetStarted();
    }

    public interface GetStartedDialogIF {
        void onClickGetStarted();
    }
}
