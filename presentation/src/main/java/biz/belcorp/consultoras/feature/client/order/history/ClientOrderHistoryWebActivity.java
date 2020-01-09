package biz.belcorp.consultoras.feature.client.order.history;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

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
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.client.di.DaggerClientComponent;
import biz.belcorp.consultoras.feature.embedded.gpr.OrderWebActivity;
import biz.belcorp.consultoras.feature.embedded.ordersfic.OrdersFicFragment;
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity;
import biz.belcorp.consultoras.sync.SyncUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientOrderHistoryWebActivity extends BaseActivity implements HasComponent<ClientComponent>
    , LoadingView, ClientOrderHistoryWebFragment.Listener {

    private ClientComponent component;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

    private int toolbarType = 1;

    private ClientOrderHistoryWebFragment fragment;

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
        setContentView(R.layout.activity_client_order_history_web);
        ButterKnife.bind(this);
        init(savedInstanceState);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddOrdersActivity.RESULT && resultCode == Activity.RESULT_OK) {
            fragment.refresh();
        }else if(requestCode == OrderWebActivity.RESULT){
            fragment.refresh();
        }
        //if (fragment != null) fragment.onActivityResult(requestCode, resultCode, data);
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
                /*if (getVisibleFragment() instanceof PedidosPendientesFragment)
                    ((PedidosPendientesFragment) getVisibleFragment())
                            .trackEvent(GlobalConstant.EVENT_CAT_SEARCH,
                                GlobalConstant.EVENT_ACTION_SEARCH_SELECTION,
                                GlobalConstant.EVENT_LABEL_NOT_AVAILABLE
                                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT);*/
                navigator.navigateToOrdersWithResult(this, menu);
            }
            return true;
        });
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
        initBundle();
        initializeToolbar();
        if (savedInstanceState == null) {
            fragment = new ClientOrderHistoryWebFragment();
            fragment.setArguments(getIntent().getExtras());
            addFragment(R.id.fltContainer, fragment);
        }

        toolbar.inflateMenu(R.menu.search_menu);
    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerClientComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(getActivityModule())
            .build();
    }

    private void initBundle() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            int clientId = extras.getInt(GlobalConstant.CLIENTE_ID, 0);
            if (clientId == 0) toolbarType = 0;
        }
    }

    private void initializeToolbar() {
        TextView tvwToolbarTitle = toolbar.findViewById(R.id.tvw_toolbar_title);
        if (toolbarType == 1)
            tvwToolbarTitle.setText(getString(R.string.client_order_history_title));
        else tvwToolbarTitle.setText(getString(R.string.client_order_history_all_title));
        toolbar.setNavigationOnClickListener(v -> {
            if (fragment != null)
                fragment.trackBackPressed();
            onBackPressed();
        });
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
    public ClientComponent getComponent() {
        return component;
    }

    /**********************************************************/

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

    /**********************************************************/

    @Override
    public void onBackPressed() {
        if (fragment != null) fragment.trackBackPressed();
        else onBackFromFragment();
    }
}
