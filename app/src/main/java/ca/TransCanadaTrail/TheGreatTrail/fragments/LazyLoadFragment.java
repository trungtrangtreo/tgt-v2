package ca.TransCanadaTrail.TheGreatTrail.fragments;

public abstract class LazyLoadFragment extends BaseTrailDrawingFragment {
    protected boolean isVisible;
    protected boolean isViewCreated;
    protected boolean isLoaded;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            this.isVisible = true;
            loadUi();
        }
    }

    abstract protected void loadUi();
}
