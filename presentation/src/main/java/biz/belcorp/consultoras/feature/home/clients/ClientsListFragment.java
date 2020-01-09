package biz.belcorp.consultoras.feature.home.clients;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.component.PagerSlidingTabStrip;
import biz.belcorp.consultoras.common.material.tap.MaterialTapTargetPrompt;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.client.registration.ClientRegistrationActivity;
import biz.belcorp.consultoras.feature.contact.ContactListActivity;
import biz.belcorp.consultoras.feature.debt.AddDebtActivity;
import biz.belcorp.consultoras.feature.home.BaseHomeFragment;
import biz.belcorp.consultoras.feature.home.clients.all.AllClientsFragment;
import biz.belcorp.consultoras.feature.home.clients.porcobrar.PorCobrarFragment;
import biz.belcorp.consultoras.feature.home.di.HomeComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MenuCode;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.util.KeyboardUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClientsListFragment extends BaseHomeFragment implements ClientsView, AllClientsFragment.ClientListListener
    , PorCobrarFragment.ClientListListener {

    @Inject
    ClientsPresenter presenter;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabLayout;
    @BindView(R.id.view_pager_content)
    ViewPager viewPager;
    @BindView(R.id.view_fab_content)
    View viewFabContent;
    @BindView(R.id.fab_add_client)
    FloatingActionButton fabAddClient;
    @BindView(R.id.llt_add_from_contact)
    LinearLayout lltAddFromContact;
    @BindView(R.id.fab_add_from_contact)
    FloatingActionButton fabAddFromContact;
    @BindView(R.id.tvw_fab_option_1)
    TextView tvwFabOption1;
    @BindView(R.id.llt_new_client)
    LinearLayout lltNewClient;
    @BindView(R.id.fab_new_client)
    FloatingActionButton fabNewClient;
    @BindView(R.id.tvw_fab_option_2)
    TextView tvwFabOption2;
    @BindView(R.id.fab_add_cobranza)
    FloatingActionButton fabAddCobranza;

    Bundle bundle;
    private String codeMenu = "";
    private int countClient;
    private int countClientDeuda;

    public static final int REQUEST_CODE_REGISTER = 200;
    public static final int REQUEST_CODE_CONTACTS = 201;

    private Boolean firstClient = true;
    private Boolean isFabOpen = false;
    private Animation animFabOpen;
    private Animation animFabClose;

    private Typeface tfRegular;
    private ClientsViewPagerAdapter viewPagerAdapter;

    private Handler refreshDataHandler;
    private Runnable refreshDataRunnable;

    private MaterialTapTargetPrompt.Builder tapNewClientTargetPromptBuilder;
    private MaterialTapTargetPrompt.Builder tapAddDebtTargetPromptBuilder;

    private boolean materialTapOpenedNewClient = false;
    private boolean materialTapOpenedAddDebt = false;

    /**********************************************************/

    public ClientsListFragment() {
        super();
    }

    public static ClientsListFragment newInstance() {
        return new ClientsListFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(HomeComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        if (savedInstanceState == null) {
            init();

            bundle = getArguments();
            if (bundle != null && codeMenu.equals("")) {
                codeMenu = bundle.getString("menu");
                if (codeMenu != null ) {
                    if (codeMenu.equals(MenuCode.DEBTS)) {
                        fabAddCobranza.setVisibility(View.VISIBLE);
                        fabAddClient.setVisibility(View.GONE);
                    } else if (codeMenu.equals(MenuCode.CLIENTS)) {
                        firstClient = false;
                        fabAddCobranza.setVisibility(View.GONE);
                        fabAddClient.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        if (viewPagerAdapter == null)
            viewPagerAdapter = new ClientsViewPagerAdapter(getChildFragmentManager(), getContext(), this, this, bundle);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setActivityContainer(getActivity());
        initControls();
        tabLayout.setViewPager(viewPager);
        tabLayout.setTypeface(tfRegular, Typeface.NORMAL);

        switch (codeMenu) {
            case MenuCode.CLIENTS:
                viewPager.setCurrentItem(0);
                break;
            case MenuCode.DEBTS:
                firstClient = true;
                viewPager.setCurrentItem(1);
                break;
            case MenuCode.CLIENTS_WITH_ORDER:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.bajada();
        trackView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != refreshDataHandler)
            refreshDataHandler.removeCallbacks(refreshDataRunnable);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (null != bundle && null != codeMenu) bundle.putString("menu", codeMenu);
        outState.putString("menu", codeMenu);
        super.onSaveInstanceState(outState);
    }

    /**********************************************************/

    private void init() {
        initAnimations();
        initMaterialTap();
        initMaterialTapCobranza();
    }

    private void initAnimations() {
        tfRegular = Typeface.createFromAsset(context().getAssets(), GlobalConstant.LATO_REGULAR_SOURCE);
        animFabOpen = AnimationUtils.loadAnimation(context(), R.anim.fab_open);
        animFabClose = AnimationUtils.loadAnimation(context(), R.anim.fab_close);
    }

    private void initControls() {

        tabLayout.setTrackPageEventClick(page -> presenter.trackEvent(getCurrentScreenName(page),
            GlobalConstant.EVENT_CAT_CLIENTS,
            GlobalConstant.EVENT_ACTION_CLIENTS,
            getEventLabel(page),
            GlobalConstant.EVENT_NAME_CLIENTS_MENU));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                // EMPTY
            }

            @Override
            public void onPageSelected(int position) {
                trackView();
                KeyboardUtil.dismissKeyboard(context(), getActivity().getCurrentFocus());
                switch (position) {
                    case 0:
                        codeMenu = MenuCode.CLIENTS;
                        fabAddClient.setVisibility(View.VISIBLE);
                        presenter.showMaterialTap();
                        fabAddCobranza.setVisibility(View.GONE);
                        fabAddClient.setVisibility(View.VISIBLE);
                        presenter.initScreenTrack(0);
                        break;
                    case 1:
                        codeMenu = MenuCode.DEBTS;
                        if(!firstClient) {
                            presenter.showMaterialTapDeuda();
                        }
                        firstClient = false;
                        fabAddCobranza.setVisibility(View.VISIBLE);
                        fabAddClient.setVisibility(View.GONE);
                        presenter.initScreenTrack(1);
                        break;
                    case 2:
                    default:
                        codeMenu = MenuCode.CLIENTS_WITH_ORDER;
                        fabAddClient.setVisibility(View.GONE);
                        fabAddCobranza.setVisibility(View.GONE);
                        presenter.initScreenTrack(2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                // EMPTY
            }
        });
    }

    public void animateFAB() {
        if (!isVisible()) return;

        if (animFabClose == null || animFabOpen == null)
            initAnimations();

        if (isFabOpen) {
            fabNewClient.startAnimation(animFabClose);
            tvwFabOption1.setVisibility(View.INVISIBLE);
            fabAddFromContact.startAnimation(animFabClose);
            tvwFabOption2.setVisibility(View.INVISIBLE);
            viewFabContent.setVisibility(View.GONE);
            fabAddFromContact.setClickable(false);
            fabNewClient.setClickable(false);
            isFabOpen = false;
        } else {
            viewFabContent.setVisibility(View.VISIBLE);
            fabAddFromContact.startAnimation(animFabOpen);
            tvwFabOption1.setVisibility(View.VISIBLE);
            fabNewClient.startAnimation(animFabOpen);
            tvwFabOption2.setVisibility(View.VISIBLE);
            fabAddFromContact.setClickable(true);
            fabNewClient.setClickable(true);
            isFabOpen = true;
        }
    }

    public void refreshLists() {
        EventBus.getDefault().post(new ClientsListEvent(true));
    }

    @OnClick(R.id.fab_add_cobranza)
    protected void onAddDebtClick() {
        hideKeyboard();
        startActivity(new Intent(getActivity(), AddDebtActivity.class));
        presenter.initScreenTrackAgregarDeuda();
    }

    /**********************************************************/

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        KeyboardUtil.dismissKeyboard(getActivity(), view);
    }

    private void trackView() {
        if (refreshDataHandler == null) initHandler();

        refreshDataHandler.removeCallbacks(refreshDataRunnable);
        refreshDataHandler.postDelayed(refreshDataRunnable, 200);
    }

    private void initHandler() {
        refreshDataHandler = new Handler();
        refreshDataRunnable = () -> {
            if(presenter != null) presenter.initScreenTrack(viewPager.getCurrentItem());
        };
    }

    /**********************************************************/

    @OnClick({ R.id.fab_new_client, R.id.tvw_fab_option_2 })
    public void registerNewClient() {
        animateFAB();
        Intent intent = new Intent(getContext(), ClientRegistrationActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
        presenter.initScreenTrackNuevoCliente();
    }

    @OnClick({ R.id.fab_add_from_contact, R.id.tvw_fab_option_1} )
    public void registerFromContact() {
        animateFAB();
        Intent intent = new Intent(getContext(), ContactListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CONTACTS);
        presenter.initScreenTrackAgregarDesdeContacto();
    }

    @OnClick(R.id.fab_add_client)
    public void addClient() {
        hideKeyboard();
        animateFAB();
        presenter.initScreenTrackBotonAgregarCliente();
    }

    @OnClick(R.id.view_fab_content)
    public void onBackgroundClick() {
        hideKeyboard();
        animateFAB();
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

    @Override
    public void onClientsSaved() {
        EventBus.getDefault().post(new ClientsListEvent(true));
    }

    @Override
    public void onBusinessError(BusinessErrorModel errorModel) {
        //Empty
    }

    @Override
    public void initScreenTrack(LoginModel loginModel, int type) {
        String screenName = getCurrentScreenName(type);

        Bundle bundleScreen = new Bundle();

        bundleScreen.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        bundleScreen.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundleScreen, properties);
    }

    @Override
    public void showMaterialTap() {
        if (countClient == 0) {
            showMaterialTapNewClient();
        }
    }

    @Override
    public void showMaterialTapDeuda() {
        if (countClientDeuda == 0) {
            showMaterialTapAddDebt();
        }
    }

    @Override
    public void initScreenTrackBotonAgregarCliente(LoginModel loginModel) {
        Tracker.Clientes.trackBotonAgregarCliente(loginModel);
    }

    @Override
    public void initScreenTrackAgregarDeuda(LoginModel loginModel) {
        Tracker.Deudas.trackConDeudaAgregarDeuda(loginModel);
    }

    @Override
    public void initScreenTrackNuevoCliente(LoginModel loginModel) {
        Tracker.Clientes.trackNuevoCliente(loginModel);
    }

    @Override
    public void initScreenTrackAgregarDesdeContacto(LoginModel loginModel) {
        Tracker.Clientes.trackAgregarDesdeContacto(loginModel);
    }

    @Override
    public void onError(ErrorModel error) {
        EventBus.getDefault().post(new ClientsListEvent(true));
    }

    /****************************************************************/

    @Override
    public void setRippleState(boolean state) {
        // EMPTY
    }

    @Override
    public void checkMaterialTap() {
        if (codeMenu != null && codeMenu.equals(MenuCode.CLIENTS))
            presenter.showMaterialTap();

    }

    @Override
    public void checkMaterialTapDeuda() {
        if (codeMenu != null && codeMenu.equals(MenuCode.DEBTS))
            presenter.showMaterialTapDeuda();
    }

    @Override
    public void setPorCobrarClientsCount(int size) {
        countClientDeuda = size;
    }

    public int getCurrentPage() {
        return viewPager.getCurrentItem();
    }

    @Override
    public void setAllClientsCount(int size) {
        countClient = size;
    }

    private String getCurrentScreenName(int page) {
        String screenName;

        switch (page) {
            case 0:
                screenName = GlobalConstant.SCREEN_CLIENTS_ALL;
                break;
            case 1:
                screenName = GlobalConstant.SCREEN_CLIENTS_DEBT;
                break;
            case 2:
                screenName = GlobalConstant.SCREEN_CLIENTS_WITH_ORDER;
                break;
            default:
                screenName = GlobalConstant.SCREEN_CLIENTS_ALL;
                break;
        }
        return screenName;
    }

    private String getEventLabel(int page) {
        String eventLabel;

        switch (page) {
            case 0:
                eventLabel = GlobalConstant.EVENT_LABEL_CLIENTS_ALL;
                break;
            case 1:
                eventLabel = GlobalConstant.EVENT_LABEL_CLIENTS_DEBT;
                break;
            case 2:
                eventLabel = GlobalConstant.EVENT_LABEL_CLIENTS_ORDER;
                break;
            default:
                eventLabel = GlobalConstant.EVENT_LABEL_CLIENTS_ALL;
                break;
        }
        return eventLabel;
    }

    public void initMaterialTap() {
        tapNewClientTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(getActivity())
            .setPrimaryText(R.string.home_material_tap_title_cliente)
            .setSecondaryText(R.string.home_material_tap_description_cliente)
            .setAnimationInterpolator(new FastOutSlowInInterpolator())
            .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
            .setPrimaryTextColour(Color.WHITE)
            .setSecondaryTextColour(Color.WHITE)
            .setPrimaryTextSize(R.dimen.material_tap_text_primary_size)
            .setSecondaryTextSize(R.dimen.material_tap_text_secondary_size)
            .setBackgroundColour(ContextCompat.getColor(getActivity(), R.color.home_material_tap_color))
            .setIconDrawableColourFilter(ContextCompat.getColor(getActivity(), R.color.home_material_tap_color))
            .setIconDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_new_client));

        tapNewClientTargetPromptBuilder.setTarget(fabAddClient);
        tapNewClientTargetPromptBuilder.setPromptStateChangeListener((prompt, state) -> {
            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                presenter.updateMaterialTap();
            }
            materialTapOpenedNewClient = false;
        });

    }

    public void showMaterialTapNewClient(){
        if (tapNewClientTargetPromptBuilder != null && !materialTapOpenedNewClient) {
            tapNewClientTargetPromptBuilder.show();
            materialTapOpenedNewClient = true;
        }
    }

    public void initMaterialTapCobranza() {
        tapAddDebtTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(getActivity())
            .setPrimaryText(R.string.home_material_tap_title_deuda)
            .setSecondaryText(R.string.home_material_tap_description_deuda)
            .setAnimationInterpolator(new FastOutSlowInInterpolator())
            .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
            .setPrimaryTextColour(Color.WHITE)
            .setSecondaryTextColour(Color.WHITE)
            .setPrimaryTextSize(R.dimen.material_tap_text_primary_size)
            .setSecondaryTextSize(R.dimen.material_tap_text_secondary_size)
            .setBackgroundColour(ContextCompat.getColor(getActivity(), R.color.home_material_tap_color))
            .setIconDrawableColourFilter(ContextCompat.getColor(getActivity(), R.color.home_material_tap_color))
            .setIconDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_cobranza));

        tapAddDebtTargetPromptBuilder.setTarget(fabAddCobranza);
        tapAddDebtTargetPromptBuilder.setPromptStateChangeListener((prompt, state) -> {
            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                presenter.updateMaterialTapDeuda();
            }
            materialTapOpenedAddDebt = false;
        });

    }

    public void showMaterialTapAddDebt(){
        if (tapAddDebtTargetPromptBuilder != null && !materialTapOpenedAddDebt) {
            tapAddDebtTargetPromptBuilder.show();
            materialTapOpenedAddDebt = true;
        }
    }

    /**********************************************************/

    public interface ClientListFragmentListener {
        void editar(ClienteModel clientemodel);

        void refreshClientsList();

        void onAddDebt();
    }
}
