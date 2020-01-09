package biz.belcorp.consultoras.feature.home.incentives;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.component.PagerSlidingTabStrip;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.home.BaseHomeFragment;
import biz.belcorp.consultoras.feature.home.di.HomeComponent;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.annotation.Country;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IncentivesContainerFragment extends BaseHomeFragment
    implements IncentivesContainerView, IncentivesContainerAdapter.TrackEventListener {

    @Inject
    IncentivesContainerPresenter presenter;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabLayout;

    @BindView(R.id.view_pager_content)
    ViewPager viewPager;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.groupDefaultView)
    RelativeLayout groupDefaultView;

    @BindArray(R.array.incentives_titles)
    String[] incentivesTitles;

    private Handler refreshDataHandler;
    private Runnable refreshDataRunnable;
    private Typeface tfRegular;

    private IncentivesContainerAdapter incentivesAdapter = null;

    private Bundle activeArguments = null;
    private Bundle historyArguments = null;

    /**********************************************************/

    public IncentivesContainerFragment() {
        super();
    }

    public static IncentivesContainerFragment newInstance() {
        return new IncentivesContainerFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(HomeComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        if (savedInstanceState == null) {
            init();
        }
        presenter.get();
        presenter.getContenidoUserDigital();
        initHandler();
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incentives_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.getOffline();
        if (refreshDataHandler == null) initHandler();

        refreshDataHandler.removeCallbacks(refreshDataRunnable);
        refreshDataHandler.postDelayed(refreshDataRunnable, 200);
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel, int type) {
        String screenName = getCurrentScreenName(type);
        Tracker.trackScreen(screenName, loginModel);
    }

    /**********************************************************/

    private void init() {
        tfRegular = Typeface.createFromAsset(context().getAssets(), GlobalConstant.LATO_REGULAR_SOURCE);
    }

    private void initHandler() {
        refreshDataHandler = new Handler();
        refreshDataRunnable = () -> {
            if (presenter != null) presenter.initScreenTrack(viewPager.getCurrentItem());
        };
    }

    private void initControls() {

        tabLayout.setTrackPageEventClick(page -> presenter.trackEvent(getCurrentScreenName(page),
            GlobalConstant.EVENT_CAT_INCENTIVES,
            GlobalConstant.EVENT_ACTION_INCENTIVES,
            getEventLabel(page),
            GlobalConstant.EVENT_NAME_INCENTIVES_MENU));

        viewPager.setAdapter(incentivesAdapter);
        tabLayout.setActivityContainer(getActivity());
        tabLayout.setViewPager(viewPager);
        tabLayout.setTypeface(tfRegular, Typeface.NORMAL);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // EMPTY
            }

            @Override
            public void onPageSelected(int position) {
                presenter.initScreenTrack(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // EMPTY
            }
        });
    }

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    @Override
    public void onError(ErrorModel errorModel) {
        processError(errorModel);
    }

    @Override
    public void showByActive(List<ConcursoModel> newProgamContest,
                             List<ConcursoModel> constanciaCurrentContest,
                             List<ConcursoModel> listaPedidoCurrentContest,
                             List<ConcursoModel> listaPedidoPreviousContest,
                             ConcursoModel brightPathContest,
                             String campaingCurrent, String countryMoneySymbol, String countrISO) {
        activeArguments = new Bundle();
        activeArguments.putParcelable(GlobalConstant.CONTEST_BRIGHT_PATH_KEY, brightPathContest);
        activeArguments.putParcelableArrayList(GlobalConstant.CONTEST_NEW_PROGRAM_KEY, (ArrayList<ConcursoModel>) newProgamContest);
        activeArguments.putParcelableArrayList(GlobalConstant.CURRENT_CONSTANCIA_KEY, (ArrayList<ConcursoModel>) constanciaCurrentContest);
        activeArguments.putParcelableArrayList(GlobalConstant.CURRENT_CONTEST_KEY, (ArrayList<ConcursoModel>) listaPedidoCurrentContest);
        activeArguments.putParcelableArrayList(GlobalConstant.PREVIOUS_CONTEST_KEY, (ArrayList<ConcursoModel>) listaPedidoPreviousContest);
        activeArguments.putString(GlobalConstant.CAMPAIGN_KEY, campaingCurrent);
        activeArguments.putString(GlobalConstant.CURRENCY_SYMBOL_KEY, countryMoneySymbol);
        activeArguments.putString(GlobalConstant.TRACK_VAR_COUNTRY, countrISO);

        initializeAdapter(countrISO);
    }

    @Override
    public void showByHistory(List<ConcursoModel> historyCurrentContest, String countrISO) {
        historyArguments = new Bundle();
        historyArguments.putParcelableArrayList(GlobalConstant.CURRENT_CONTEST_KEY, (ArrayList<ConcursoModel>) historyCurrentContest);
        historyArguments.putString(GlobalConstant.TRACK_VAR_COUNTRY, countrISO);
    }

    @Override
    public void initializeAdapter(String countryISO) {
        if (isAdded()) {
            if (Country.CO.equals(countryISO)) {
                incentivesTitles = getResources().getStringArray(R.array.incentives_titles_co);
            }

            if (!isAdded()) return;

            incentivesAdapter = new IncentivesContainerAdapter(getChildFragmentManager(), incentivesTitles);
            incentivesAdapter.setArguments(activeArguments, historyArguments);
            incentivesAdapter.setTrackListener(this);
            viewPager.setAdapter(null);
            initControls();
        }
    }

    /**********************************************************************************************/

    @Override
    public void track(String screenName, String label, String eventName) {
        presenter.trackEvent(screenName,
            GlobalConstant.EVENT_CAT_INCENTIVES_BONIFICATION,
            GlobalConstant.EVENT_ACTION_INCENTIVES_BONIFICATION,
            label,
            eventName);
    }

    @Override
    public void showDefaultContainer() {
        groupDefaultView.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
    }

    @Override
    public void loadViewBonifDigital(String url) {
        groupDefaultView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    /**********************************************************************************************/

    public int getCurrentPage() {
        return viewPager.getCurrentItem();
    }

    private String getCurrentScreenName(int page) {
        String screenName;

        switch (page) {
            case 0:
                screenName = GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER;
                break;
            case 1:
                screenName = GlobalConstant.SCREEN_INCENTIVES_GIFT_HISTORIC;
                break;
            default:
                screenName = GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER;
                break;
        }

        return screenName;
    }

    private String getEventLabel(int page) {

        switch (page) {
            case 0:
                return GlobalConstant.EVENT_LABEL_INCENTIVES_BONIFICATION;
            case 1:
                return GlobalConstant.EVENT_LABEL_INCENTIVES_HISTORIC;
            default:
                return GlobalConstant.EVENT_LABEL_INCENTIVES_BONIFICATION;
        }
    }
}
