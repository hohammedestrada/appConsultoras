package biz.belcorp.consultoras.feature.home.tracking;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.component.PagerSlidingTabStrip;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.tracking.TrackingModel;
import biz.belcorp.consultoras.feature.home.tracking.di.TrackingComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.annotation.DatetimeFormat;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TrackingFragment extends BaseFragment implements TrackingView {

    private static final String TAG = "TrackingFragment";

    @Inject
    TrackingPresenter presenter;

    @BindView(R.id.rlt_content_list_null)
    RelativeLayout rltNoStockouts;

    @BindView(R.id.tvw_message)
    TextView tvwMessage;

    @BindView(R.id.rlt_content)
    RelativeLayout rltContent;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabLayout;

    @BindView(R.id.view_pager_content)
    ViewPager viewPager;

    private Typeface tfRegular;
    private TrackingViewPagerAdapter viewPagerAdapter;
    private Unbinder bind;

    /**********************************************************/

    public TrackingFragment() {
        super();
    }

    public static TrackingFragment newInstance() {
        return new TrackingFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(TrackingComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        init();

        Bundle bundle = getArguments();

        if (bundle != null) {
            int top = bundle.getInt(GlobalConstant.TRACKING_TOP);
            presenter.data(top);
        }
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        bind = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        if (bind != null)
            bind.unbind();

        if (presenter != null)
            presenter.destroy();

        super.onDestroyView();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_SEGUIMIENTO_PEDIDOS);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /**********************************************************/

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    private void init() {
        tfRegular = Typeface.createFromAsset(context().getAssets(), GlobalConstant.LATO_REGULAR_SOURCE);
    }

    @Override
    public void showTracking(List<TrackingModel> trackingModels) {
        if (trackingModels != null && !trackingModels.isEmpty()) {
            if (viewPagerAdapter == null)
                viewPagerAdapter = new TrackingViewPagerAdapter(getChildFragmentManager(), getActivity(), trackingModels);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setActivityContainer(getActivity());
            tabLayout.setViewPager(viewPager);
            tabLayout.setTypeface(tfRegular, Typeface.NORMAL);

            int campaign = getArguments().getInt(GlobalConstant.CAMPAIGN_KEY);
            String dateCampaign = getArguments().getString(GlobalConstant.DATE_KEY);

            if (campaign == 0) {
                viewPager.setCurrentItem(trackingModels.size());
            } else {
                boolean find = false;

                for (int pos = 0; pos < trackingModels.size(); pos++) {
                    if (trackingModels.get(pos).getCampania().equals(campaign) && viewPagerAdapter.convertFecha(trackingModels.get(pos).getFecha()).equals(viewPagerAdapter.convertFecha(dateCampaign))) {
                        find = true;
                        viewPager.setCurrentItem(pos);
                    }
                }

                if (!find) {
                    viewPager.setCurrentItem(trackingModels.size());
                }
            }

            rltContent.setVisibility(View.VISIBLE);
            rltNoStockouts.setVisibility(View.GONE);
        } else {
            if (NetworkUtil.isThereInternetConnection(context()))
                tvwMessage.setText(R.string.tracking_no_items);
            else
                tvwMessage.setText(R.string.tracking_no_items_conexion);
            tvwMessage.setVisibility(View.VISIBLE);

            rltContent.setVisibility(View.GONE);
            rltNoStockouts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        processError(throwable);
    }

    /****************************************************************/

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_SEGUIMIENTO_PEDIDOS);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }
}
