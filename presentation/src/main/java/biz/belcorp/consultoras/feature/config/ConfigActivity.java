package biz.belcorp.consultoras.feature.config;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
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
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.feature.config.di.ConfigComponent;
import biz.belcorp.consultoras.feature.config.di.DaggerConfigComponent;
import biz.belcorp.consultoras.sync.SyncUtil;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigActivity extends BaseActivity implements HasComponent<ConfigComponent>,
    LoadingView {

    private ConfigComponent component;

    @BindView(R.id.view_connection)
    View vwConnection;
    @BindView(R.id.ivw_connection)
    ImageView ivwConnection;
    @BindView(R.id.tvw_connection_message)
    TextView tvwConnectionMessage;
    @BindView(R.id.view_loading)
    View vwLoading;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ConfigFragment configFragment;

    /**********************************************************/

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ConfigActivity.class);
    }

    public void goToHome() {
        finish();
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
        setContentView(R.layout.activity_config);
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
        if (configFragment != null)
            configFragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToHome();
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
            configFragment = new ConfigFragment();
            addFragment(R.id.fltContainer, configFragment);
        }
    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerConfigComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(getActivityModule())
            .build();
    }

    private void initializeToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvwToolbarTitle = toolbar.findViewById(R.id.tvw_toolbar_title);
        tvwToolbarTitle.setText(getString(R.string.config_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
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
    public ConfigComponent getComponent() {
        return component;
    }
}
