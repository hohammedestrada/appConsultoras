package biz.belcorp.consultoras.feature.home.incentives;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class IncentivesContainerAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles;

    private GiftActiveFragment giftActiveFragment;
    private GiftHistoryFragment giftHistoryFragment;

    private Bundle activeArguments;
    private Bundle historyArguments;
    private TrackEventListener trackListener;


    IncentivesContainerAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
        giftActiveFragment = GiftActiveFragment.Companion.newInstance();
        giftHistoryFragment = GiftHistoryFragment.Companion.newInstance();
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int pos) {

        Fragment fragment;

        if (pos == 0) {
            if (null == giftActiveFragment) {
                giftActiveFragment = GiftActiveFragment.Companion.newInstance();
                giftActiveFragment.setArguments(activeArguments);
                giftActiveFragment.setTrackListener(trackListener);
            }
            fragment = giftActiveFragment;
        } else {
            if (null == giftHistoryFragment) {
                giftHistoryFragment = GiftHistoryFragment.Companion.newInstance();
                giftHistoryFragment.setArguments(historyArguments);
                giftHistoryFragment.setTrackListener(trackListener);
            }
            fragment = giftHistoryFragment;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    /***********************************************************************************************/

    void setArguments(Bundle activeArguments, Bundle historyArguments) {
        this.activeArguments = activeArguments;
        this.historyArguments = historyArguments;

        giftActiveFragment.setArguments(activeArguments);
        giftHistoryFragment.setArguments(historyArguments);
    }

    void setTrackListener(TrackEventListener trackListener) {
        this.trackListener = trackListener;

        giftActiveFragment.setTrackListener(trackListener);
        giftHistoryFragment.setTrackListener(trackListener);
    }

    public interface TrackEventListener {
        void track(String screenName, String label, String eventName);
    }
}
