package biz.belcorp.consultoras.feature.client.note;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Pattern;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseActivity;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.network.NetworkEvent;
import biz.belcorp.consultoras.common.sync.SyncEvent;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.client.di.DaggerClientComponent;
import biz.belcorp.consultoras.sync.SyncUtil;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteActivity extends BaseActivity implements HasComponent<ClientComponent>, NoteFragment.NotaListener, LoadingView {

    public static final String CLIENTE = "cliente";
    public static final String CLIENT_ID = "clientID";
    public static final String CLIENT_LOCAL_ID = "clientLocalID";
    public static final String CLIENT_NAME = "clientName";
    public static final String NOTA = "NOTA";
    public static final String NOTA_ID = "notaID";
    public static final int REQUEST_CODE = 999;
    public static final String ACCION = "nota_accion";

    public static final int NUEVO_CLIENTE = 0;
    public static final int CLIENTE_EXISTENTE = 1;
    public static final int NUEVA_NOTA = 2;
    public static final int REQUEST_CODE_EDIT = 998;

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
    @BindView(R.id.tvw_toolbar_title)
    TextView tvwToolbarTitle;

    private NoteFragment fragment;
    private ClientComponent component;

    String clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        initToolbar();
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
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
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

    /**********************************************************************************************/

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

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fg = getVisibleFragment();
                if (fg != null)
                    ((NoteFragment)fg).trackBackPressed();
                else
                    onBackPressed();
            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initializeInjector();
        clientName = getIntent().getStringExtra(CLIENT_NAME);
        if (savedInstanceState == null) {

            fragment = new NoteFragment();
            fragment.setArguments(getIntent().getExtras());
            addFragment(R.id.fltContainer, fragment);
        }
        if (clientName != null) {
            String[] parts = clientName.toUpperCase().split(Pattern.quote(" "));
            if (parts.length > 1)
                tvwToolbarTitle.setText("NOTA PARA " + parts[0] + " " + parts[1]);
            else tvwToolbarTitle.setText("NOTA PARA " + parts[0]);

        } else {
            tvwToolbarTitle.setText("NUEVA NOTA");
        }
    }

    @Override
    protected void initializeInjector() {
        component = DaggerClientComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(getActivityModule())
            .build();
    }

    @Override
    protected void initControls() {
        // EMPTY
    }

    @Override
    protected void initEvents() {
        // EMPTY
    }

    @Override
    public ClientComponent getComponent() {
        return component;
    }

    /**********************************************************************************************/

    @Override
    public void onNotaCreada(AnotacionModel anotacionModel) {
        setResult(RESULT_OK, getIntent().putExtra(NOTA, anotacionModel));
        finish();
    }

    @Override
    public void onNotaActualizada(AnotacionModel anotacionModel) {
        setResult(RESULT_OK, getIntent().putExtra(NOTA, anotacionModel));
        finish();
    }

    @Override
    public void onBackFromFragment() {
        onBackPressed();
    }

    /**********************************************************************************************/

    @Override
    public void showLoading() {
        vwLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        vwLoading.setVisibility(View.GONE);
    }
}
