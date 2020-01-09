package biz.belcorp.consultoras.common.animation.ripple;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import biz.belcorp.library.log.BelcorpLogger;

/**
 *
 */
class RippleLifeCycle extends FragmentManager.FragmentLifecycleCallbacks {

    private RippleView shapeRipple;
    private Fragment fragment;

    RippleLifeCycle(RippleView shapeRipple) {
        super();
        this.shapeRipple = shapeRipple;
    }

    void attachListener(Fragment fragment) {
        if (shapeRipple == null) {
            BelcorpLogger.a("Shape Ripple is null, activity listener is not attached!!");
            return;
        }

        this.fragment = fragment;
        this.fragment.getFragmentManager().registerFragmentLifecycleCallbacks(this, false);
    }

    private void detachListener() {
        if (fragment == null) {
            return;
        }

        fragment.getFragmentManager().unregisterFragmentLifecycleCallbacks(this);
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        if (shapeRipple == null) {
            return;
        }

        shapeRipple.restartRipple();
        BelcorpLogger.a("Activity is Resumed");
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        if (shapeRipple == null) {
            return;
        }

        shapeRipple.stop();
        BelcorpLogger.a("Activity is Paused");
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentDestroyed(fm, f);

        detachListener();
        BelcorpLogger.a("Activity is Destroyed");
    }
}
