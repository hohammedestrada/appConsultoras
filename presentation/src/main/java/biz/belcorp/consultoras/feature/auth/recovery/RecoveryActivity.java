package biz.belcorp.consultoras.feature.auth.recovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import biz.belcorp.consultoras.feature.auth.di.AuthComponent;
import biz.belcorp.consultoras.feature.auth.di.DaggerAuthComponent;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecoveryActivity extends BaseActivity implements HasComponent<AuthComponent>, LoadingView, RecoveryFragment.RecoveryFragmentListener {

    private AuthComponent component;

    @BindView(R.id.rlt_recovery)
    RelativeLayout rltRecovery;
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

    /**********************************************************/

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RecoveryActivity.class);
    }

    public void goToLogin() {
        Bundle extras = getIntent().getExtras();
        this.getNavigator().navigateToLogin(this, extras);
    }

    /**********************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        ButterKnife.bind(this);

        init(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NetworkUtil.isThereInternetConnection(this)) {
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
        } else {
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

    @Override
    protected void init(Bundle savedInstanceState) {
        initializeInjector();
        initializeToolbar();
        if (savedInstanceState == null) {
            Fragment recoveryFragment = new RecoveryFragment();
            addFragment(R.id.fltContainer, recoveryFragment);
        }
    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerAuthComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(getActivityModule())
            .build();
    }

    private void initializeToolbar() {
        TextView tvwToolbarTitle = toolbar.findViewById(R.id.tvw_toolbar_title);
        tvwToolbarTitle.setText(getString(R.string.recovery_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getVisibleFragment();
                if (fragment != null)
                    ((RecoveryFragment)fragment).trackBackPressed();
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
    public AuthComponent getComponent() {
        return component;
    }

    /**********************************************************/

    @Override
    public void onLogin() {
        goToLogin();
    }
}
