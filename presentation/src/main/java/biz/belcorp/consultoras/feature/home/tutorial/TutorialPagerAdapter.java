package biz.belcorp.consultoras.feature.home.tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author andres.escobar on 21/08/2017.
 */
class TutorialPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> tutorialModelList;

    TutorialPagerAdapter(FragmentManager fm, List<Fragment> tutorialModelList) {
        super(fm);
        this.tutorialModelList = tutorialModelList;
    }

    @Override
    public Fragment getItem(int position) {
        return tutorialModelList.get(position);
    }

    @Override
    public int getCount() {
        return tutorialModelList.size();
    }
}
