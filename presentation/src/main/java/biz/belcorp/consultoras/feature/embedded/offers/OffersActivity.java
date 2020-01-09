package biz.belcorp.consultoras.feature.embedded.offers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseActivity;
import biz.belcorp.consultoras.common.network.NetworkEvent;
import biz.belcorp.consultoras.common.sync.SyncEvent;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.feature.embedded.offers.di.DaggerOffersComponent;
import biz.belcorp.consultoras.feature.embedded.offers.di.OffersComponent;
import biz.belcorp.consultoras.feature.embedded.ordersfic.OrdersFicFragment;
import biz.belcorp.consultoras.feature.splash.SplashActivity;
import biz.belcorp.consultoras.sync.SyncUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MenuCode;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OffersActivity extends BaseActivity
    implements HasComponent<OffersComponent>,
    OffersFragment.Listener, LoadingView {

    public static final String OPTION = "opcion";
    public static final String SECTION = "seccion";
    public static final String URL = "url";

    public static final int RESULT = 1001;

    private OffersComponent component;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvw_toolbar_title)
    TextView tvwToolbar;
    @BindView(R.id.view_connection)
    View vwConnection;
    @BindView(R.id.ivw_connection)
    ImageView ivwConnection;
    @BindView(R.id.tvw_connection_message)
    TextView tvwConnectionMessage;
    @BindView(R.id.view_loading)
    View vwLoading;
    @BindView(R.id.view_loading_sync)
    View vwLoadingSync;

    private OffersFragment fragment;
    private MenuItem searchItem;

    /**********************************************************/

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, OffersActivity.class);
    }

    public void showSearchOption(){
        searchItem.setVisible(true);
    }


    /**********************************************************/

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_client_order_web);
        ButterKnife.bind(this);
        init(savedInstanceState);
        dynamicLinks();
    }

    private void dynamicLinks() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, pendingDynamicLinkData -> {
            Uri deepLink;
            if (pendingDynamicLinkData != null) {
                deepLink = pendingDynamicLinkData.getLink();
                BelcorpLogger.d("getDynamicLink onSuccess " + deepLink.toString(), "");
            }

        }).addOnFailureListener(this, e -> BelcorpLogger.d("getDynamicLink onFailure " + e.getMessage(), ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtil.isThereInternetConnection(this))
            setStatusTopNetwork();
        else {
            vwConnection.setVisibility(View.VISIBLE);
            tvwConnectionMessage.setText(R.string.connection_offline);
            ivwConnection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**********************************************************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent event) {
        switch (event.getEvent()) {
            case NetworkEventType.CONNECTION_AVAILABLE:
                SyncUtil.triggerRefresh();
                setStatusTopNetwork();
                break;
            case NetworkEventType.CONNECTION_NOT_AVAILABLE:
                vwConnection.setVisibility(View.VISIBLE);
                tvwConnectionMessage.setText(R.string.connection_offline);
                ivwConnection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert));
                break;
            case NetworkEventType.DATAMI_AVAILABLE:
                vwConnection.setVisibility(View.VISIBLE);
                tvwConnectionMessage.setText(getString(R.string.connection_datami_available));
                ivwConnection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet));
                break;
            case NetworkEventType.DATAMI_NOT_AVAILABLE:
            case NetworkEventType.WIFI:
            default:
                vwConnection.setVisibility(View.GONE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncEvent(SyncEvent event) {
        if (event.isSync()) {
            vwLoadingSync.setVisibility(View.VISIBLE);
        } else {
            vwLoadingSync.setVisibility(View.GONE);
        }
    }

    /**********************************************************/

    private void setStatusTopNetwork() {
        switch (ConsultorasApp.getInstance().getDatamiType()) {
            case NetworkEventType.DATAMI_AVAILABLE:
                vwConnection.setVisibility(View.VISIBLE);
                tvwConnectionMessage.setText(getString(R.string.connection_datami_available));
                ivwConnection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet));
                break;
            case NetworkEventType.DATAMI_NOT_AVAILABLE:
            case NetworkEventType.WIFI:
            default:
                vwConnection.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initializeInjector();
        initializeToolbar();
        if (savedInstanceState == null) {
            fragment = new OffersFragment();
            fragment.setArguments(getIntent().getExtras());
            addFragment(R.id.fltContainer, fragment, false, MenuCode.OFFERS);
        }

        toolbar.inflateMenu(R.menu.search_menu);
        searchItem = toolbar.getMenu().findItem(R.id.item_search);

    }


    @Override
    public void onGetMenu(Menu menu) {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.item_search) {
                if (getVisibleFragment() instanceof OrdersFicFragment)
                    ((OrdersFicFragment) getVisibleFragment())
                        .trackEvent(GlobalConstant.EVENT_CAT_SEARCH,
                            GlobalConstant.EVENT_ACTION_SEARCH_SELECTION,
                            GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
                            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT);

                getNavigator().navigateToSearch(this);
            }else if(item.getItemId() == R.id.item_orders){
                /*if (getVisibleFragment() instanceof OffersFragment)
                    ((OffersFragment) getVisibleFragment())
                            .trackEvent(GlobalConstant.EVENT_CAT_SEARCH,
                                GlobalConstant.EVENT_ACTION_SEARCH_SELECTION,
                                GlobalConstant.EVENT_LABEL_NOT_AVAILABLE
                                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT);*/
                navigator.navigateToOrdersWithResult(this, menu);
            }
            return true;
        });

    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerOffersComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(getActivityModule())
            .build();
    }

    private void initializeToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            if (fragment != null)
                fragment.trackBackPressed();
            else onBackFromFragment();
        });
    }

    public void setTitle(String title){
        tvwToolbar.setText(title);
    }

    @Override
    protected void initControls() {
        // EMPTY
    }

    @Override
    protected void initEvents() {
        // EMPTY
    }

    /**********************************************************/

    @Override
    public void showLoading() {
        vwLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        vwLoading.setVisibility(View.GONE);
    }


    /**********************************************************/

    @Override
    public OffersComponent getComponent() {
        return component;
    }

    @Override
    public void onBackFromFragment() {
        boolean goBack = fragment.onBackWebView();
        if (!goBack) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (null != upIntent  && NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
            }
            setResult(Activity.RESULT_OK, upIntent);
            finish();
        }
    }

    /** */

    @Override
    public void onBackPressed() {
        if (fragment != null)
            fragment.trackBackPressed();
        else onBackFromFragment();
    }

    @Override
    public void onGanaMasFinish() {
        finish();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConstant.RESTART_SCREEN, true);
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
