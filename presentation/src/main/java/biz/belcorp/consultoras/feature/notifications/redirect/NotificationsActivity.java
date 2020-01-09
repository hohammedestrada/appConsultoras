package biz.belcorp.consultoras.feature.notifications.redirect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import biz.belcorp.consultoras.base.BaseActivity;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity;
import biz.belcorp.consultoras.feature.campaigninformation.CampaignInformationActivity;
import biz.belcorp.consultoras.feature.dreammeter.DreamMeterActivity;
import biz.belcorp.consultoras.feature.embedded.academia.AcademiaActivity;
import biz.belcorp.consultoras.feature.announcement.AnnouncementActivity;
import biz.belcorp.consultoras.feature.catalog.CatalogContainerActivity;
import biz.belcorp.consultoras.feature.embedded.changes.ChangesActivity;
import biz.belcorp.consultoras.feature.embedded.closeout.CloseoutActivity;
import biz.belcorp.consultoras.feature.embedded.esikaahora.EsikaAhoraWebActivity;
import biz.belcorp.consultoras.feature.embedded.gpr.OrderWebActivity;
import biz.belcorp.consultoras.feature.embedded.tuvoz.TuVozOnlineWebActivity;
import biz.belcorp.consultoras.feature.home.HomeActivity;
import biz.belcorp.consultoras.feature.home.accountstate.AccountStateActivity;
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity;
import biz.belcorp.consultoras.feature.home.fest.FestActivity;
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackActivity;
import biz.belcorp.consultoras.feature.home.myorders.MyOrdersActivity;
import biz.belcorp.consultoras.feature.embedded.ordersfic.OrdersFicActivity;
import biz.belcorp.consultoras.feature.home.profile.MyProfileActivity;
import biz.belcorp.consultoras.feature.home.subcampaing.FestSubCampaingActivity;
import biz.belcorp.consultoras.feature.home.tracking.TrackingActivity;
import biz.belcorp.consultoras.feature.notifications.di.DaggerNotificationsComponent;
import biz.belcorp.consultoras.feature.notifications.di.NotificationsComponent;
import biz.belcorp.consultoras.feature.embedded.offers.OffersActivity;
import biz.belcorp.consultoras.feature.splash.SplashActivity;
import biz.belcorp.consultoras.feature.stockouts.StockoutsActivity;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.OtherAppUtil;
import biz.belcorp.consultoras.util.anotation.AdRedirectCode;
import biz.belcorp.consultoras.util.anotation.FromOpenActivityType;
import biz.belcorp.consultoras.util.anotation.MenuCode;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import biz.belcorp.consultoras.util.anotation.NotificationCode;
import biz.belcorp.consultoras.util.anotation.PageUrlType;

public class NotificationsActivity extends BaseActivity implements HasComponent<NotificationsComponent>,
    NotificationsFragment.OnNotificationListener {

    private NotificationsComponent component;
    private NotificationsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initializeInjector();
        if (savedInstanceState == null) {
            fragment = new NotificationsFragment();
            if (null != getIntent().getExtras())
                fragment.setArguments(getIntent().getExtras());
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragtrans = fm.beginTransaction();
            fragtrans.add(fragment, "Notification");
            fragtrans.commit();
        }
    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerNotificationsComponent.builder()
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
    public NotificationsComponent getComponent() {
        return component;
    }

    /**********************************************************/

    @Override
    public void initScreen(boolean isLogged, int optionOrder) {

        Bundle extras = getIntent().getExtras();
        String notificationCode = getIntent().getStringExtra(GlobalConstant.NOTIFICATION_CODE);
        int notification_id = getIntent().getIntExtra(GlobalConstant.NOTIFICATION_ID, 0);

        if (fragment != null) {
            fragment.presenter.updateNotificacion(notification_id, 1);
        }

        Intent intent;

        if (isLogged && notificationCode != null) {
            switch (notificationCode) {
                case NotificationCode.ORDER:
                    if (extras != null)
                        extras.putInt(GlobalConstant.NOTIFICATION_ORDER_OPTION_CODE, optionOrder);
                    if (optionOrder == 1) intent = new Intent(this, OrderWebActivity.class);
                    else if (optionOrder == 2) intent = new Intent(this, AddOrdersActivity.class);
                    else intent = new Intent(this, HomeActivity.class);
                    break;
                case NotificationCode.OFFERS_SHOWROOM:
                    intent = new Intent(this, OffersActivity.class);
                    intent.putExtra(OffersActivity.OPTION, PageUrlType.SHOW_ROOM);
                    break;
                case NotificationCode.OFFERS_NEW_NEW:
                    intent = new Intent(this, OffersActivity.class);
                    intent.putExtra(OffersActivity.OPTION, PageUrlType.LO_NUEVO_NUEVO);
                    break;
                case NotificationCode.OFFERS_FOR_YOU:
                    intent = new Intent(this, OffersActivity.class);
                    intent.putExtra(OffersActivity.OPTION, PageUrlType.OFERTAS_PARA_TI);
                    break;
                case NotificationCode.OFFERS_ONLY_TODAY:
                    intent = new Intent(this, OffersActivity.class);
                    intent.putExtra(OffersActivity.OPTION, PageUrlType.SOLO_HOY);
                    break;
                case NotificationCode.OFFERS_SALES_TOOLS:
                    intent = new Intent(this, OffersActivity.class);
                    intent.putExtra(OffersActivity.OPTION, PageUrlType.HERRAMIENTAS_DE_VENTA);
                    break;
                case NotificationCode.OFFERS_INFORMATION:
                    intent = new Intent(this, OffersActivity.class);
                    intent.putExtra(OffersActivity.OPTION, PageUrlType.REVISTA_DIGITAL_INFO);
                    break;
                case NotificationCode.OFFERS_THE_MOST_WINNING:
                    intent = new Intent(this, OffersActivity.class);
                    intent.putExtra(OffersActivity.OPTION, PageUrlType.THE_MOST_WINNING);
                    break;
                case NotificationCode.OFFERS_PERFECT_DUO:
                    intent = new Intent(this, OffersActivity.class);
                    intent.putExtra(OffersActivity.OPTION, PageUrlType.PERFECT_DUO);
                    break;
                case NotificationCode.CATALOG:
                    intent = new Intent(this, CatalogContainerActivity.class);
                    break;
                case NotificationCode.ACCOUNT:
                    intent = new Intent(this, AccountStateActivity.class);
                    break;
                case NotificationCode.ANNOUNCEMENT:
                    intent = new Intent(this, AnnouncementActivity.class);
                    intent.putExtra(AnnouncementActivity.OPTION, notificationCode);
                    break;
                case NotificationCode.MY_ACADEMY:
                    intent = new Intent(this, AcademiaActivity.class);
                    break;
                case NotificationCode.MY_PROFILE:
                    intent = new Intent(this, MyProfileActivity.class);
                    break;
                case NotificationCode.STOCKOUTS:
                    intent = new Intent(this, StockoutsActivity.class);
                    break;
                case NotificationCode.CHANGE:
                    intent = new Intent(this, ChangesActivity.class);
                    break;
                case NotificationCode.ORDERS_FIC:
                    intent = new Intent(this, OrdersFicActivity.class);
                    break;
                case NotificationCode.MY_ORDER:
                    intent = new Intent(this, TrackingActivity.class);
                    intent.putExtra(GlobalConstant.TRACKING_TOP, TrackingActivity.TRACKING_TOP_3);
                    break;
                case NotificationCode.CLOSE_OUT:
                    intent = new Intent(this, CloseoutActivity.class);
                    break;
                case NotificationCode.OFFERS:
                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(HomeActivity.Companion.getMENU_OPTION(), MenuCodeTop.OFFERS);
                    break;
                case NotificationCode.CAMINO_BRILLANTE_LANDING:
                    intent = new Intent(this, CaminoBrillanteActivity.class);
                    break;
                case NotificationCode.CAMINO_BRILLANTE_OFERTAS_ESPECIALES:
                    intent = new Intent(this, CaminoBrillanteActivity.class);
                    intent.putExtra(CaminoBrillanteActivity.OPTION, PageUrlType.OFERTAS_ESPECIALES);
                    break;
                case NotificationCode.CAMINO_BRILLANTE_MEDALLAS:
                    intent = new Intent(this, CaminoBrillanteActivity.class);
                    intent.putExtra(CaminoBrillanteActivity.OPTION, PageUrlType.MEDALLAS);
                    break;

                case NotificationCode.DREAM_METER_LANDING:
                    intent = new Intent(this, DreamMeterActivity.class);
                    break;
                case NotificationCode.ASESOR_DE_BELLEZA:
                    if (OtherAppUtil.INSTANCE.isMaquilladorInstalled(context())) {
                        intent = getPackageManager().getLaunchIntentForPackage(OtherAppUtil.MQVIRTUAL_PACKAGE_NAME);
                    } else {
                        intent = OtherAppUtil.INSTANCE.getMaquilladorPlayStoreIntent();
                    }
                    break;
                case NotificationCode.ARMA_TU_PACK:
                    intent = new Intent(this, ArmaTuPackActivity.class);
                    break;
                case NotificationCode.TU_VOZ_ONLINE:
                    intent = new Intent(this, TuVozOnlineWebActivity.class);
                    break;
                case NotificationCode.CALENDARIO_FACTURACION:
                    intent = new Intent(this, CampaignInformationActivity.class);
                    break;
                case NotificationCode.CONFERENCIA_DIGITAL:
                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(MenuCode.CONFERENCIA_DIGITAL, AdRedirectCode.CONFERENCIA_DIGITAL);
                    break;
                case NotificationCode.FEST_LANDING:
                    intent = new Intent(this, FestActivity.class);
                    break;
                case NotificationCode.ESIKA_AHORA:
                    intent = new Intent(this, EsikaAhoraWebActivity.class);
                    break;
                case NotificationCode.SUB_CAMPAIGN_LANDING:
                    intent = new Intent(this, FestSubCampaingActivity.class);
                    break;
                default:
                    intent = new Intent(this, HomeActivity.class);
                    break;
            }
        } else {
            intent = new Intent(this, SplashActivity.class);
        }

        if (extras != null) intent.putExtras(extras);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
