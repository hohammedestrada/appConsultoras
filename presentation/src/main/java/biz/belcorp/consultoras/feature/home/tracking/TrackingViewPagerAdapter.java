package biz.belcorp.consultoras.feature.home.tracking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.common.model.tracking.TrackingModel;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;

class TrackingViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "TrackingViewPagerAdapter";
    private List<String> tabTitles;
    private Context context;
    private List<TrackingModel> trackingModels;
    private TrackingDetailFragment[] fragments;

    TrackingViewPagerAdapter(FragmentManager fm, Context context, List<TrackingModel> trackingModels) {
        super(fm);
        this.context = context;
        this.trackingModels = trackingModels;
        this.tabTitles = loadTabsTitles(trackingModels);
        this.fragments = new TrackingDetailFragment[trackingModels.size()];
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public Fragment getItem(int position) {

        if (trackingModels.size() > position) {

            TrackingModel model = trackingModels.get(position);

            Bundle bundle = new Bundle();
            bundle.putParcelable(GlobalConstant.TRACKING_KEY, model);

            TrackingDetailFragment trackingDetailFragment = new TrackingDetailFragment();
            trackingDetailFragment.setArguments(bundle);
            return trackingDetailFragment;
        } else
            return new TrackingDetailFragment();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        fragments[position] = (TrackingDetailFragment) super.instantiateItem(container, position);
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    private List<String> loadTabsTitles(List<TrackingModel> trackingModels) {
        tabTitles = new ArrayList<>();

        for (TrackingModel trackingModel : trackingModels) {

            String campaing = "";
            if (null != trackingModel.getCampania()) {
                campaing = Integer.toString(trackingModel.getCampania());

                if (campaing.length() > 4)
                    campaing = campaing.substring(4);
            }

            String campaingDate = "";
            if (null != trackingModel.getFecha() && !trackingModel.getFecha().isEmpty()) {
                campaingDate = convertFecha(trackingModel.getFecha());
            }


            tabTitles.add("C" + campaing + " - " + StringUtil.capitalize(campaingDate));
        }

        return tabTitles;
    }

    public String convertFecha(String fecha) {

        String finalDate = "";

        try {
            finalDate = DateUtil.convertFechaToString(DateUtil.convertirISODatetoDate(fecha), "dd MMM");
        } catch (ParseException e) {
            BelcorpLogger.w(TAG, "convertFecha", e);
        }

        return finalDate;
    }

    public Context getContext() {
        return context;
    }
}
