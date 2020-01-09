package biz.belcorp.consultoras.feature.client.card;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.Map;
import java.util.regex.Pattern;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseActivity;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ContactoModel;
import biz.belcorp.consultoras.common.network.NetworkEvent;
import biz.belcorp.consultoras.common.sync.SyncEvent;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.client.di.DaggerClientComponent;
import biz.belcorp.consultoras.feature.client.edit.ClientEditActivity;
import biz.belcorp.consultoras.sync.SyncUtil;
import biz.belcorp.consultoras.util.AppBarStateChangeListener;
import biz.belcorp.consultoras.util.CommunicationUtils;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.NetworkUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ClientCardActivity extends BaseActivity implements HasComponent<ClientComponent>,
    LoadingView, ClientCardFragment.ClientCardFragmentListener {

    private static final String TAG = "ClientCardActivity";
    public static final String RELOAD = "reload";

    @BindView(R.id.tvw_profile)
    TextView tvwProfile;
    @BindView(R.id.tvw_name)
    TextView tvwName;
    @BindView(R.id.tvw_birthday)
    TextView tvwBirthday;
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
    @BindView(R.id.chk_favorite)
    ImageView chkFavorite;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.ctlLayout)
    CollapsingToolbarLayout collapsingLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rlt_collapse)
    RelativeLayout rltCollapse;
    @BindView(R.id.tvw_toolbar_title)
    TextView tvwToolbarTitle;

    /**************/
    @BindView(R.id.ivw_enviar_sms)
    ImageView ivwSendSMS;
    @BindView(R.id.ivw_enviar_whatsapp)
    ImageView ivwSendWhatsApp;
    @BindView(R.id.ivw_enviar_correo)
    ImageView ivwSendMail;
    @BindView(R.id.ivw_llamar)
    ImageView ivwSendCall;

    @BindView(R.id.rlt_llamar)
    RelativeLayout rltllamar;
    @BindView(R.id.rlt_sms)
    RelativeLayout rltSms;
    @BindView(R.id.rlt_correo)
    RelativeLayout rltCorreo;
    @BindView(R.id.rlt_whatsapp)
    RelativeLayout rltWhatsapp;
    @BindView(R.id.llt_birthday)
    LinearLayout lltBirthday;

    @BindView(R.id.tvw_correo)
    TextView tvwCorreo;
    @BindView(R.id.tvw_llamar)
    TextView tvwLlamar;
    @BindView(R.id.tvw_sms)
    TextView tvwSms;
    @BindView(R.id.tvw_whatsapp)
    TextView tvwWhatsapp;

    private ClientComponent component;

    private ClientCardFragment fragment;
    private ClienteModel clienteModel;

    private static final int REQUEST_CODE_EDIT = 150;

    private Drawable starOn;
    private Drawable starOff;

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
        setContentView(R.layout.activity_client_card);

        ButterKnife.bind(this);
        startToolbar();
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
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
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
            if (fragment != null)
                fragment.refreshData();
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

    private void startToolbar() {
        appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.equals(State.COLLAPSED) && clienteModel != null) {
                    rltCollapse.setVisibility(View.GONE);
                    collapsingLayout.setTitleEnabled(true);
                    tvwToolbarTitle.setVisibility(View.VISIBLE);

                    String name = clienteModel.getNombres() + (clienteModel.getApellidos() == null ? "" : " " + clienteModel.getApellidos());
                    name = name.trim();
                    String[] parts = name.split(Pattern.quote(" "));

                    String nameTitle = StringUtil.subString(parts[0], 0, 19);

                    if (parts.length > 1)
                        nameTitle = StringUtil.subString(parts[0] + " " + parts[1], 0, 19);

                    tvwToolbarTitle.setText(nameTitle);
                } else {
                    tvwToolbarTitle.setVisibility(View.GONE);
                    rltCollapse.setVisibility(View.VISIBLE);
                    collapsingLayout.setTitleEnabled(false);
                }
            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initializeInjector();
        if (savedInstanceState == null) {
            fragment = new ClientCardFragment();
            fragment.setArguments(getIntent().getExtras());

            if (getIntent().getBooleanExtra(RELOAD, false)) {
                fragment.showAnnotationsByDefault();
            }
            addFragment(R.id.fltContainer, fragment);
        }

        starOn = ContextCompat.getDrawable(this, R.drawable.ic_star_filled);
        starOff = ContextCompat.getDrawable(this, R.drawable.ic_star);
        chkFavorite.setOnClickListener(v -> {
            showLoading();

            clienteModel.setFavorito(clienteModel.getFavorito() == 1 ? 0 : 1);
            fragment.saveFavorite(clienteModel);
        });
    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerClientComponent.builder()
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
    public void onClientSet(ClienteModel clienteModel) {
        this.clienteModel = clienteModel;

        if (checkContactState(clienteModel.getContactoModelMap().get(ContactType.PHONE)) &&
            checkContactState(clienteModel.getContactoModelMap().get(ContactType.MOBILE))) {
            ivwSendCall.setBackgroundResource(R.drawable.ic_circle_client_gray);
            tvwLlamar.setTextColor(ContextCompat.getColor(this, R.color.gray_disabled));
        } else {
            ivwSendCall.setBackgroundResource(R.drawable.ic_circle_black);
            tvwLlamar.setTextColor(Color.BLACK);
        }

        if (checkContactState(clienteModel.getContactoModelMap().get(ContactType.EMAIL))) {
            ivwSendMail.setBackgroundResource(R.drawable.ic_circle_client_gray);
            tvwCorreo.setTextColor(ContextCompat.getColor(this, R.color.gray_disabled));
        } else {
            ivwSendMail.setBackgroundResource(R.drawable.ic_circle_black);
            tvwCorreo.setTextColor(Color.BLACK);
        }

        if (checkContactState(clienteModel.getContactoModelMap().get(ContactType.MOBILE))) {
            ivwSendSMS.setBackgroundResource(R.drawable.ic_circle_client_gray);
            ivwSendWhatsApp.setBackgroundResource(R.drawable.ic_circle_client_gray);
            tvwSms.setTextColor(ContextCompat.getColor(this, R.color.gray_disabled));
            tvwWhatsapp.setTextColor(ContextCompat.getColor(this, R.color.gray_disabled));
        } else {
            ivwSendSMS.setBackgroundResource(R.drawable.ic_circle_black);
            ivwSendWhatsApp.setBackgroundResource(R.drawable.ic_circle_black);
            tvwSms.setTextColor(Color.BLACK);
            tvwWhatsapp.setTextColor(Color.BLACK);
        }

        String name = clienteModel.getNombres() + (clienteModel.getApellidos() == null ? "" : " " + clienteModel.getApellidos());
        tvwName.setText(name.trim());

        if (!TextUtils.isEmpty(clienteModel.getFechaNacimiento())) {
            lltBirthday.setVisibility(View.VISIBLE);
            try {
                String finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil
                    .convertirDDMMAAAAtoDate(clienteModel.getFechaNacimiento()), "dd MMM"));
                tvwBirthday.setText(finalDate);
            } catch (ParseException e) {
                Log.e(TAG, "ClientCard", e);
            }
        } else {
            lltBirthday.setVisibility(View.GONE);
        }

        tvwProfile.setText(StringUtil.getTwoFirstInitials(name));

        chkFavorite.setImageDrawable(clienteModel.getFavorito().equals(1) ? starOn : starOff);
    }

    @Override
    public void onClientSaved(Boolean result) {
        hideLoading();

        boolean star = chkFavorite.getDrawable().equals(starOn);

        chkFavorite.setImageDrawable(star ? starOff : starOn);

        if (result && !star) {
            Toast.makeText(this, getString(R.string.client_favorito_ok), Toast.LENGTH_SHORT).show();
        } else if (result) {
            Toast.makeText(this, getString(R.string.client_favorito_delete), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClientDeleted() {
        back();
    }

    /**********************************************************/

    @OnClick(R.id.rlt_sms)
    public void enviarSms() {
        Map<Integer, ContactoModel> contactoModelMap = clienteModel.getContactoModelMap();
        if (contactoModelMap.get(ContactType.MOBILE) != null
            && !contactoModelMap.get(ContactType.MOBILE).getValor().isEmpty()) {
            String number = contactoModelMap.get(ContactType.MOBILE).getValor();
            CommunicationUtils.enviarSms(this, number, "");
        }
    }

    @OnClick(R.id.rlt_whatsapp)
    public void enviarWhatsapp() {
        if (CommunicationUtils.isWhatsappInstalled(this)) {
            Map<Integer, ContactoModel> contactoModelMap = clienteModel.getContactoModelMap();
            if (contactoModelMap.get(ContactType.MOBILE) != null
                && !contactoModelMap.get(ContactType.MOBILE).getValor().isEmpty()) {
                String number = contactoModelMap.get(ContactType.MOBILE).getValor();
                CommunicationUtils.enviarWhatsapp(this, CountryUtil.getDialCode(this) + number, "");
            }
        } else {
            new AlertDialog.Builder(this)
                .setMessage(R.string.whatsapp_not_installed)
                .setPositiveButton(R.string.button_aceptar, (dialog, button) -> CommunicationUtils.openWhatsappPlayStore(ClientCardActivity.this))
                .setNegativeButton(R.string.button_cancelar, (dialog, button) -> dialog.dismiss())
                .show();
        }
    }

    @OnClick(R.id.rlt_correo)
    public void enviarCorreo() {
        Map<Integer, ContactoModel> contactoModelMap = clienteModel.getContactoModelMap();
        if (contactoModelMap.get(ContactType.EMAIL) != null
            && !contactoModelMap.get(ContactType.EMAIL).getValor().isEmpty()) {
            String to = contactoModelMap.get(ContactType.EMAIL).getValor();
            CommunicationUtils.enviarCorreo(this, to, "");
        }
    }

    @OnClick(R.id.rlt_llamar)
    public void llamarEvent() {

        boolean allowCall = (clienteModel.getContactoModelMap().get(ContactType.MOBILE) != null &&
            !TextUtils.isEmpty(clienteModel.getContactoModelMap().get(ContactType.MOBILE).getValor())) ||
            (clienteModel.getContactoModelMap().get(ContactType.PHONE) != null &&
                !TextUtils.isEmpty(clienteModel.getContactoModelMap().get(ContactType.PHONE).getValor()));

        if (allowCall){
            Map<Integer, ContactoModel> contactoModelMap = clienteModel.getContactoModelMap();
            if (contactoModelMap.get(ContactType.MOBILE) != null || contactoModelMap.get(ContactType.PHONE) != null) {

                String phone;

                if (clienteModel.getTipoContactoFavorito() == null || clienteModel.getTipoContactoFavorito().equals(0))
                    phone = contactoModelMap.containsKey(ContactType.MOBILE) ? contactoModelMap.get(ContactType.MOBILE).getValor() : contactoModelMap.get(ContactType.PHONE).getValor();
                else
                    phone = contactoModelMap.get(clienteModel.getTipoContactoFavorito().equals(ContactType.PHONE) ? ContactType.PHONE : ContactType.MOBILE).getValor();

                CommunicationUtils.llamar(this, phone);
            }
        }
    }

    @OnClick(R.id.tvw_back)
    public void back() {
        if (fragment != null)
            fragment.trackBackPressed();
        else
            finish();
    }

    @OnClick(R.id.tvw_editar)
    public void editar() {
        Intent intent = new Intent(this, ClientEditActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GlobalConstant.CLIENTE_ID, getIntent().getIntExtra(GlobalConstant.CLIENTE_ID, -1));
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    /**********************************************************************************************/

    private boolean checkContactState(ContactoModel model) {
        return model == null || TextUtils.isEmpty(model.getValor());
    }

    /****************************************************************/

}
