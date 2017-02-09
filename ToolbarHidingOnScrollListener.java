package jp.moremal.moremall.event;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * A OnScrollListener that hides the toolbar on scroll events.
 * Created by Daniel Veihelmann on 15.02.2015.
 */
public class ToolbarHidingOnScrollListener extends RecyclerView.OnScrollListener {

    private final View toolbarContainer;
    private final View toolbar;
    private final View parallaxScrollingView;
    private final View lastToolbarView;

    private float parallaxScrollingFactor = 0.7f;

    public ToolbarHidingOnScrollListener(View toolbarContainer, View toolbar, View lastToolbarView) {
        this(toolbarContainer, toolbar, lastToolbarView, null);
    }

    public ToolbarHidingOnScrollListener(View toolbarContainer, View toolbar, View lastToolbarView, View parallaxScrollingView) {
        this.toolbarContainer = toolbarContainer;
        this.toolbar = toolbar;
        this.parallaxScrollingView = parallaxScrollingView;
        this.lastToolbarView = lastToolbarView;

        //toolbarContainer.setBackgroundColor(Color.alpha(0));
    }

    public void setParallaxScrollingFactor(float parallaxScrollingFactor) {
        this.parallaxScrollingFactor = parallaxScrollingFactor;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            /*
            if (Math.abs(toolbarContainer.getTranslationY()) > lastToolbarView.getHeight()) {
                hideToolbar();
            } else {
                showToolbar();
            }
            */
            showToolbar();
        } else {
            toolbarContainer.clearAnimation();
        }

    }

    protected void showToolbar() {
        toolbarContainer.clearAnimation();
        toolbarContainer
                .animate()
                .translationY(0)
                .start();

        parallaxScrollingView.clearAnimation();
        parallaxScrollingView
                .animate()
                .translationY(0)
                .start();
    }

    private void hideToolbar() {
        toolbarContainer.clearAnimation();
        toolbarContainer
                .animate()
                .translationY(-lastToolbarView.getBottom())
                .start();

        parallaxScrollingView.clearAnimation();
        parallaxScrollingView
                .animate()
                .translationY(-parallaxScrollingView.getBottom())
                .start();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //scrollColoredViewParallax(dy);
        Log.v("onScrolled", " : " + dy + "");
        if (dy > 0) {
            hideToolbarBy(dy);
        } else {
            showToolbarBy(dy);
        }
    }

    private void scrollColoredViewParallax(int dy) {
        if (parallaxScrollingView != null) {
            int absoluteTranslationY = (int) (parallaxScrollingView.getTranslationY() - dy * parallaxScrollingFactor);
            parallaxScrollingView.setTranslationY(Math.min(absoluteTranslationY, 0));
        }
    }


    private void hideToolbarBy(int dy) {
        if (cannotHideMore(dy)) {
            toolbarContainer.setTranslationY(-lastToolbarView.getBottom());

            parallaxScrollingView.setTranslationY(-parallaxScrollingView.getBottom());
        } else {
            toolbarContainer.setTranslationY(toolbarContainer.getTranslationY() - dy);

            parallaxScrollingView.setTranslationY(parallaxScrollingView.getTranslationY() - dy);
        }
    }

    private boolean cannotHideMore(int dy) {
        //return Math.abs(toolbarContainer.getTranslationY() - dy) > lastToolbarView.getBottom();
        return Math.abs(parallaxScrollingView.getTranslationY() - dy) > parallaxScrollingView.getBottom();
    }


    protected void showToolbarBy(int dy) {
        if (cannotShowMore(dy)) {
            toolbarContainer.setTranslationY(0);
            parallaxScrollingView.setTranslationY(0);
        } else {
            toolbarContainer.setTranslationY(toolbarContainer.getTranslationY() - dy);
            parallaxScrollingView.setTranslationY(parallaxScrollingView.getTranslationY() - dy);
        }
    }

    private boolean cannotShowMore(int dy) {
        return toolbarContainer.getTranslationY() - dy > 0;
        //return parallaxScrollingView.getTranslationY() - dy > 0;
    }
}