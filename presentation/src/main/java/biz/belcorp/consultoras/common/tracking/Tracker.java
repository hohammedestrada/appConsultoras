package biz.belcorp.consultoras.common.tracking;

import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.orders.OfertaFinalModel;
import biz.belcorp.consultoras.common.model.product.ProductItem;
import biz.belcorp.consultoras.domain.entity.EstrategiaCarrusel;
import biz.belcorp.consultoras.domain.entity.ProductCUV;
import biz.belcorp.consultoras.domain.entity.Promotion;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.feature.home.marquee.MarqueeItem;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.Belcorp;
import biz.belcorp.consultoras.util.Constant;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.util.CurrencyUtil;
import biz.belcorp.mobile.components.design.carousel.promotion.model.PromotionModel;

/**
 * @author andres.escobar on 23/11/2017.
 */
public class Tracker {

    Tracker() {
        // EMPTY
    }

    public static void trackView(String screenName, LoginModel model) {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(model);

        BelcorpAnalytics.trackScreenView(GlobalConstant.TRACK_VAR_SCREEN_VIEW, bundle, properties);
    }

    public static void trackView(String screenName, User user) {
        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(user);

        BelcorpAnalytics.trackScreenView(GlobalConstant.TRACK_VAR_SCREEN_VIEW, bundle, properties);
    }

    public static void trackScreen(String screenName, LoginModel model) {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(model);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    public static void trackScreenUser(String screenName, User model) {

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(model);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    public static void trackBackEvent(String screenName, LoginModel model) {
        Bundle event = new Bundle();
        event.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        event.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        event.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        event.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        event.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(model);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, event, properties);
    }

    public static void trackBackEvent(String screenName) {
        Bundle event = new Bundle();
        event.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        event.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        event.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        event.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        event.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, event);
    }

    public static void trackEvent(String screenName,
                                  String category,
                                  String action,
                                  String label,
                                  LoginModel model) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, category);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, action);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, label);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(model);

        BelcorpAnalytics.trackEvent("home_options", analytics);
    }

    public static void trackEvent(String screenName,
                                  String category,
                                  String action,
                                  String label,
                                  String eventName,
                                  LoginModel model) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, category);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, action);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, label);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(model);

        BelcorpAnalytics.trackEvent(eventName, analytics, properties);
    }

    public static void trackEvent(String screenName,
                                  String category,
                                  String action,
                                  String label,
                                  String eventName,
                                  User user) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, category);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, action);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, label);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(user);

        BelcorpAnalytics.trackEvent(eventName, analytics, properties);
    }

    public static final class Home {

        Home() {
            // EMPTY
        }

        public static void trackMenu(LoginModel loginModel, String screenName) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_TOP);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_MENU);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_MENU, analytics, properties);
        }

        public static void trackMenuOption(LoginModel loginModel, int menuType, String eventLabel,
                                           String eventName, String screenName) {
            String eventCategory;

            switch (menuType) {
                case 0:
                    eventCategory = GlobalConstant.EVENT_CATEGORY_LATERAL;
                    break;
                case 1:
                    eventCategory = GlobalConstant.EVENT_CATEGORY_TOP;
                    break;
                default:
                    eventCategory = GlobalConstant.EVENT_CATEGORY_TOP;
            }

            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, eventCategory);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_OPTION);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, eventLabel);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(eventName, analytics, properties);
        }


        public static void trackMenuOption(LoginModel loginModel, int menuType, String eventLabel,
                                           String eventName, String screenName, String action) {

            String eventCategory;

            switch (menuType) {
                case 0:
                    eventCategory = GlobalConstant.EVENT_CATEGORY_LATERAL;
                    break;
                case 1:
                    eventCategory = GlobalConstant.EVENT_CATEGORY_TOP;
                    break;
                default:
                    eventCategory = GlobalConstant.EVENT_CATEGORY_TOP;
            }

            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, eventCategory);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, action);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, eventLabel);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(eventName, analytics, properties);

        }


        public static void trackHomeCard(int type, String label) {
            String event;
            String action;

            switch(type){
                case 1:{
                    event = GlobalConstant.EVENT_SHOW_SECTION;
                    action = GlobalConstant.ACTION_SHOW_SECTION;}
                    break;

                case 2:{
                    event = GlobalConstant.EVENT_VIEW_MORE;
                    action = GlobalConstant.EVENT_ACTION_BUTTON;}
                    break;

                    default:{
                        event = GlobalConstant.EVENT_MENU_NAVIGATION;
                        action = GlobalConstant.ACTION_MENU_NAVIGATION;}
            }

            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_HOME);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_HOME);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, action);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, label);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            BelcorpAnalytics.trackEvent(event, analytics);
        }

        public static void trackBackPressed(LoginModel loginModel, String screenName) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
        }

        public static void trackViewBannerLanzamiento(MarqueeItem item, int position) {
            Bundle promotion = new Bundle();
            promotion.putString(FirebaseAnalytics.Param.ITEM_ID, item.getId());
            promotion.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getName());
            promotion.putString(FirebaseAnalytics.Param.CREATIVE_NAME, GlobalConstant.EVENT_NAME_BANNER);
            promotion.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, GlobalConstant.EVENT_PREFIX_HOME + (position + 1));

            ArrayList promotions = new ArrayList<Bundle>();
            promotions.add(promotion);

            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putParcelableArrayList("promotions", promotions);

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_ITEM, ecommerceBundle);
        }

        public static void trackClickBannerLanzamiento(MarqueeItem item, int position) {
            Bundle promotion = new Bundle();
            promotion.putString(FirebaseAnalytics.Param.ITEM_ID, GlobalConstant.EVENT_NAME_BANNER_ID);
            promotion.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getName());
            promotion.putString(FirebaseAnalytics.Param.CREATIVE_NAME, GlobalConstant.EVENT_NAME_BANNER);
            promotion.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, GlobalConstant.EVENT_PREFIX_HOME + (position + 1));

            ArrayList promotions = new ArrayList<Bundle>();
            promotions.add(promotion);

            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putParcelableArrayList("promotions", promotions);

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle);
        }
    }

    public static final class Clientes {

        Clientes() {
            // EMPTY
        }

        public static void trackSeleccionarCliente(LoginModel loginModel, String screenName) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VER_CLIENTE);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_VIEW, analytics, properties);
        }

        public static void trackBotonAgregarCliente(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_TODOS);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_MENU_SECUNDARIO);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_VER_MENU);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_NEW, analytics, properties);
        }

        public static void trackNuevoCliente(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_TODOS);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_MENU_SECUNDARIO);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NUEVO_CLIENTE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_NEW, analytics, properties);
        }

        public static void trackAgregarDesdeContacto(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_TODOS);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_MENU_SECUNDARIO);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_AGREGAR_DESDE_CONTACTO);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_NEW, analytics, properties);
        }

        public static void trackImportarAgregar(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_IMPORTAR_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_IMPORTAR_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_AGREGAR);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_IMPORT, analytics, properties);
        }

        public static void trackAgregarGuardar(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_AGREGAR_CLIENTE);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_AGREGAR_CLIENTE);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_AGREGAR);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_NEW_SAVE, analytics, properties);
        }

        public static void trackFichaGestionarDeuda(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_FICHA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_FICHA);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_GESTIONAR_DEUDA);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_FILE, analytics, properties);
        }

        public static void trackFichaIngresarPedido(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_FICHA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_FICHA);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_INGRESAR_PEDIDO);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_FILE, analytics, properties);
        }

        public static void trackFichaRevisarPedidos(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_FICHA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_CLIENTES);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_FICHA);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_REVISAR_PEDIDOS);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_FILE, analytics, properties);
        }
    }

    public static final class DetalleDeuda {

        DetalleDeuda() {
            // EMPTY
        }

        public static void trackAnadirRecordatorio(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_BOTON);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_ANADIR_RECORDATORIO);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackAnadirDeuda(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_BOTON);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_ANADIR_DEUDA);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackRegistrarUnPago(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_BOTON);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_REGISTRAR_UN_PAGO);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackEnviarDetalleDeuda(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_BOTON);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_ENVIAR_DETALLE_DE_DEUDA);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackDeudaAgregada(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VER_DETALLE);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_DEUDA_AGREGADA);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackPedidoBelcorp(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VER_DETALLE);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_PEDIDO_BELCORP);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackPago(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DETALLE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VER_DETALLE);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_PAGO);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

    }

    public static final class Deudas {

        Deudas() {
            // EMPTY
        }

        public static void trackConDeudaAgregarDeuda(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_CON_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DEUDAS);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_BOTON);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_AGREGAR_DEUDA);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackAgregarDeudaExitoso(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_AGREGAR_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DEUDAS);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_AGREGAR_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_REGISTRO_EXITOSO);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackRegistrarPagoExitoso(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_REGISTRAR_PAGO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DEUDAS);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_REGISTRAR_PAGO);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_REGISTRO_EXITOSO);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackEnviarMensajeDePago(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_ENVIAR_PAGO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DEUDAS);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_ENVIAR_MENSAJE_DE_PAGO);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackEnviarMensajeDeDeuda(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_ENVIAR_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DEUDAS);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_ENVIAR_MENSAJE_DE_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }

        public static void trackActualizarDeuda(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_CLIENTES_EDITAR_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_DEUDAS);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_ACTUALIZAR_DEUDA);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CLIENT_DEBT, analytics, properties);
        }
    }

    public static final class Pedido {

        Pedido() {
            // EMPTY
        }

        public static void trackBotonVerMas(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_BOTON);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_VER_TODAS_LAS_OFERTAS);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIEW_MORE, analytics, properties);
        }

        public static void trackBotonReservar(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_RESERVA_EXITOSA);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_RESERVATION_COMPLETE, analytics, properties);
        }

        public static void trackComboSeleccionCliente(LoginModel loginModel) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_SELECCIONAR_CLIENTE);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_CHOOSE_CLIENT, analytics, properties);
        }

        public static void trackTabClienteProducto(LoginModel loginModel, String nombreTab) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_SELECCIONAR_TAB);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, nombreTab);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_TAB_SELECTION, analytics, properties);
        }

        public static void trackBotonBuscarProducto(LoginModel loginModel, String codigoProducto) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BUSCAR_PRODUCTO);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, codigoProducto);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_SEARCH, analytics, properties);
        }

        public static void trackBotonModificarPedido(LoginModel loginModel, String pedidoId) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_PEDIDO_RESERVADO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_MODIFICAR_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, pedidoId);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_RESERVATION_ALTER, analytics, properties);
        }

        public static void trackVisualizarProducto(LoginModel loginModel, String nombreProducto) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VISUALIZAR_PRODUCTO);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, nombreProducto);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_PRODUCT_VIEW, analytics, properties);
        }

        public static void trackVisualizarProductoOF(LoginModel loginModel, String nombreProducto) {
            Bundle analytics = new Bundle();
            analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CATEGORY_PEDIDO);
            analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VISUALIZAR_PRODUCTO_O_F);
            analytics.putString(GlobalConstant.EVENT_VAR_LABEL, nombreProducto);
            analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

            Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_PRODUCT_VIEW, analytics, properties);
        }

        public static void trackAgregarProducto(LoginModel model, ProductItem item, int brandId,
                                                boolean isVoiceSearch) {
            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getDescripcionProd());
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE);
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, Belcorp.INSTANCE
                .getBrandById(brandId));
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioUnidad() != null ?
                item.getPrecioUnidad().doubleValue() : 0.0);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                .getCurrencyByISO(model.getCountryISO()));
            product.putLong(FirebaseAnalytics.Param.QUANTITY, item.getCantidad() != null ?
                item.getCantidad() : 0);
            if (isVoiceSearch)
                product.putString(GlobalConstant.PALANCA, GlobalConstant.ANALYTICS_AGREGAR_POR_VOZ);
            else if (item.getEtiquetaProducto() != null && !item.getEtiquetaProducto().isEmpty())
                product.putString(GlobalConstant.PALANCA, item.getEtiquetaProducto());
            else
                product.putString(GlobalConstant.PALANCA, GlobalConstant.NOT_AVAILABLE);

            Bundle properties = AnalyticsUtil.getUserProperties(model);

            Bundle ecommerce = new Bundle();
            ecommerce.putBundle(GlobalConstant.ITEMS, product);
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerce, properties);
        }

        public static void trackEliminarProducto(LoginModel model, ProductItem item) {
            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getDescripcionProd());
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE);
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, GlobalConstant.NOT_AVAILABLE);
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioUnidad() != null ?
                item.getPrecioUnidad().doubleValue() : 0.0);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                .getCurrencyByISO(model.getCountryISO()));
            product.putLong(FirebaseAnalytics.Param.QUANTITY, item.getCantidad() != null ?
                item.getCantidad() : 0);
            product.putString(GlobalConstant.PALANCA, item.getEtiquetaProducto());

            Bundle properties = AnalyticsUtil.getUserProperties(model);

            Bundle eCommerce = new Bundle();
            eCommerce.putBundle(GlobalConstant.ITEMS, product);
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART,
                eCommerce, properties);
        }
    }

    public static final class Product {

        static final String LIST_OFFERS = Constant.BRAND_FOCUS_NAME + " | Ofertas";
        static final String LIST_FINAL_OFFER = Constant.BRAND_FOCUS_NAME + " | Oferta Final";

        Product() {
            // EMPTY
        }

        // OFERTAS

        public static void add(EstrategiaCarrusel item, LoginModel model) {
            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getDescripcionCUV());
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "");
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getDescripcionMarca());
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioFinal() != null ?
                item.getPrecioFinal().doubleValue() : 0.0);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                .getCurrencyByISO(model.getCountryISO()));
            product.putLong(FirebaseAnalytics.Param.QUANTITY, item.getCantidad());
            product.putString(GlobalConstant.PALANCA, "OPM");

            Bundle properties = AnalyticsUtil.getUserProperties(model);

            Bundle eCommerce = new Bundle();
            eCommerce.putBundle(GlobalConstant.ITEMS, product);
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, eCommerce, properties);
        }

        public static void click(EstrategiaCarrusel item, int position, LoginModel model) {
            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getDescripcionCUV());
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "");
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getDescripcionMarca());
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioFinal() != null
                ? item.getPrecioFinal().doubleValue() : 0.00);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                .getCurrencyByISO(model.getCountryISO()));
            product.putInt(FirebaseAnalytics.Param.INDEX, position);

            Bundle properties = AnalyticsUtil.getUserProperties(model);

            Bundle eCommerce = new Bundle();
            eCommerce.putBundle(GlobalConstant.ITEMS, product);
            eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, LIST_OFFERS);

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT,
                eCommerce, properties);
        }

        public static void impressionOffers(LoginModel model, List<EstrategiaCarrusel> items) {

            ArrayList<Bundle> bundles = new ArrayList<>();

            for (EstrategiaCarrusel item : items) {
                Bundle product = new Bundle();
                product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
                product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getDescripcionCUV());
                product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "");
                product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART);
                product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getDescripcionMarca());
                product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioFinal() != null
                    ? item.getPrecioFinal().doubleValue() : 0.00);
                product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                    .getCurrencyByISO(model.getCountryISO()));
                product.putLong(FirebaseAnalytics.Param.INDEX, item.getIndex());

                bundles.add(product);

            }

            Bundle properties = AnalyticsUtil.getUserProperties(model);

            Bundle eCommerce = new Bundle();
            eCommerce.putParcelableArrayList(GlobalConstant.ITEMS, bundles);
            eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, LIST_OFFERS);

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS,
                eCommerce, properties);
        }

        // OFERTA FINAL

        public static void add(OfertaFinalModel item, User user) {
            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getNombreComercial());
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "");
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getNombreMarca());
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioCatalogo() != null ?
                item.getPrecioCatalogo().doubleValue() : 0.0);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                .getCurrencyByISO(user.getCountryISO() != null ? user.getCountryISO() : ""));
            product.putLong(FirebaseAnalytics.Param.QUANTITY, item.getCantidad());
            product.putString(GlobalConstant.PALANCA, "Oferta Final");

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            Bundle eCommerce = new Bundle();
            eCommerce.putBundle(GlobalConstant.ITEMS, product);
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, eCommerce, properties);
        }


        public static void click(OfertaFinalModel item, int position, User user) {
            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getNombreComercial());
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "");
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getNombreMarca());
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioCatalogo() != null
                ? item.getPrecioCatalogo().doubleValue() : 0.00);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                .getCurrencyByISO(user.getCountryISO() != null ? user.getCountryISO() : ""));
            product.putInt(FirebaseAnalytics.Param.INDEX, position + 1);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            Bundle eCommerce = new Bundle();
            eCommerce.putBundle(GlobalConstant.ITEMS, product);
            eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, LIST_FINAL_OFFER);

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT,
                eCommerce, properties);
        }

        public static void impressionFinalOffers(User user, List<OfertaFinalModel> items) {

            ArrayList<Bundle> bundles = new ArrayList<>();

            for (OfertaFinalModel item : items) {
                Bundle product = new Bundle();
                product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
                product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getNombreComercial());
                product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "");
                product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART);
                product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getNombreMarca());
                product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioCatalogo() != null
                    ? item.getPrecioCatalogo().doubleValue() : 0.00);
                product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                    .getCurrencyByISO(user.getCountryISO() != null ? user.getCountryISO() : ""));
                product.putLong(FirebaseAnalytics.Param.INDEX, item.getIndex());

                bundles.add(product);

            }

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            Bundle eCommerce = new Bundle();
            eCommerce.putParcelableArrayList(GlobalConstant.ITEMS, bundles);
            eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, LIST_FINAL_OFFER);

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS,
                eCommerce, properties);
        }

    }

    public static class CaminoBrillante {

        public static void clicVerBeneficios(LoginModel user) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_HOME);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_BOTON);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_VER_BENEFICIOS);
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_HOME);
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.BUILD_TYPE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.VER_BENEFICIOS, params, properties);
        }

        public static void selectNivel(User user, String nameNivel) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_SELECCIONAR_NIVEL);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, "Nivel: " + nameNivel);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.SELECT_LEVEL, params, properties);
        }

        public static void verNivel(User user, String nameNivel) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VER_POP_UP_NIVEL);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, "Nivel: " + nameNivel);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.VIEW_POPUP_NIVEL, params, properties);
        }

        public static void cerrarNivel(User user, String nameNivel) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CERRAR_POP_UP_NIVEL);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, "Nivel: " + nameNivel);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.CLOSE_POPUP_NIVEL, params, properties);
        }

        public static void selectOfertasEspeciales(User user) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS_MIS_BENEFICIOS);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_SELECCION_OFERTAS_ESPECIALES);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.SELECT_OFERTAS_ESPECIALES, params, properties);
        }

        public static void onAddProduct(ProductCUV item, int quantity, String nameList, User user) {
            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getDescription());
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, nameList);
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ESTANDAR);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getDescripcionMarca());
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioCatalogo() != null ? item.getPrecioCatalogo() : 0.0);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.getCountryISO() != null ? user.getCountryISO() : ""));
            product.putLong(FirebaseAnalytics.Param.QUANTITY, quantity);

            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putBundle(GlobalConstant.ITEMS, product);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerceBundle, properties);
        }

        public static void selectDetalleLogro(User user, String detalle) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS_MIS_LOGROS);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, "Detalle " + detalle.toLowerCase());
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.SELECT_UNDERSCORE + detalle.toLowerCase(), params, properties);
        }

        public static void selectComoLograrlo(User user, String logro, String indicador) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS_MIS_LOGROS);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, "Seleccionar " + logro + " – " + indicador);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.SELECCION_COMO_LOGRARLO);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.SELECT_COMO_LOGRARLO, params, properties);
        }

        public static void mostrarPopUpComoLograrlo(User user, String logro, String indicador) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS_MIS_LOGROS);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, "Ver " + logro + " – " + indicador);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.VER_DETALLE_COMO_LOGRARLO);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.VIEW_POPUP_LOGROS, params, properties);
        }

        public static void cerrarPopUpComoLograrlo(User user, String logro, String indicador) {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS_MIS_LOGROS);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, logro + " – " + indicador);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.SELECT_ENTENDIDO);

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(GlobalConstant.CLOSE_POPUP_LOGROS, params, properties);
        }

        public static void impressionItems(User user, String nameList, List<ProductCUV> items) {
            ArrayList<Bundle> bundles = new ArrayList<>();

            for (ProductCUV item : items) {
                Bundle product = new Bundle();
                product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
                product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getDescription());
                product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, nameList);
                product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ESTANDAR);
                product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getDescripcionMarca());
                product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioCatalogo() != null ? item.getPrecioCatalogo() : 0.00);
                product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.getCountryISO() != null ? user.getCountryISO() : ""));
                product.putLong(FirebaseAnalytics.Param.INDEX, item.getIndex() + 1);

                bundles.add(product);
            }

            Bundle eCommerce = new Bundle();
            eCommerce.putParcelableArrayList(GlobalConstant.ITEMS, bundles);
            eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, eCommerce, properties);
        }

        public static void onSelectProduct(User user, String nameList, ProductCUV item) {
            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getCuv());
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getDescription());
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, nameList);
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ESTANDAR);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.getDescripcionMarca());
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.getPrecioCatalogo() != null ? item.getPrecioCatalogo() : 0.0);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.getCountryISO() != null ? user.getCountryISO() : ""));
            product.putLong(FirebaseAnalytics.Param.QUANTITY, item.getIndex() + 1);

            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putBundle(GlobalConstant.ITEMS, product);

            Log.d("CB_SELECT_PRODUCT", ecommerceBundle.toString());

            Bundle properties = AnalyticsUtil.getUserProperties(user);
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle, properties);
        }
    }

    public static class Notificaciones{
        public static void iconNotificaciones(){
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_TRACK_NOTIFICACIONES_MENU_SUPERIOR);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_TRACK_NOTIFICACIONES);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_TRACK_NOT_AVAILABLE);
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.EVENT_TRACK_NOTIFICACIONES_HOME);
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.BUILD_TYPE);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_MENU_NAVIGATION, params);
        }

        public static void selectNotificacion(){
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_TRACK_NOTIFICACIONES_MENU_SUPERIOR);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_TRACK_NOTIFICACIONES);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_TRACK_SHOW_NOTIFICATIONS);
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.EVENT_CAT_REMINDER);
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.BUILD_TYPE);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, params);
        }

        public static void iconMiPerfil(){
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_TRACK_NOTIFICACIONES_HOME);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_TRACK_EDIT_PROFILE);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_TRACK_NOT_AVAILABLE);
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.EVENT_TRACK_NOTIFICACIONES_HOME);
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.BUILD_TYPE);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, params);
        }
    }


    public static class EligeTuRegalo {

        public static void clickGift() {
            Bundle params = new Bundle();
            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_PEDIDO);
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_BOTON);
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_VER_REGALOS);
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO);
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.BUILD_TYPE);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, params);
        }

        public static void selectGift(String itemId,
                                      String itemName,
                                      String itemCategory,
                                      String itemVariant,
                                      String price,
                                      String quantity,
                                      String iso){

            Bundle product = new Bundle();
            product.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, itemCategory);
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, itemVariant);
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, Constant.BRAND_FOCUS_NAME);
            product.putString(FirebaseAnalytics.Param.PRICE, price);
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
                .getCurrencyByISO(iso));
            product.putString(FirebaseAnalytics.Param.QUANTITY, quantity);

            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putBundle("items", product);

            ecommerceBundle.putString( FirebaseAnalytics.Param.ITEM_LIST, GlobalConstant.EVENT_NOMBRE_LISTA);

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerceBundle);
        }

        public static void confirmSelectGift(String ButtomName){

            Bundle product = new Bundle();
            product.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_PEDIDO);
            product.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_CLICK_BUTTON);
            product.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_POP_UP_PREFIX + ButtomName.trim());
            product.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.EVENT_INGRESAR_PEDIDO_POP_UP);
            product.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.BUILD_TYPE);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, product);
        }

    }

    public static class Survey{
        public static void trackScreenEncuesta(User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_NOMBRE_PANTALLA1);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SURVEY_ANALTICS_ACTION_PANTALLA_VISTA, extras, properties);
        }

        public static void trackScreenGracias(User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SURVEY_ANALTICS_CALIFICACION_COMPLETADO);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SURVEY_ANALTICS_ACTION_PANTALLA_VISTA, extras, properties);
        }

        public static void onClickBotonConfirmar(String tipoCalificacion, String motivo, String pantallaDondeSeEjecutoEvento, User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_SB);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, tipoCalificacion);
            extras.putString(GlobalConstant.EVENT_VAR_LABEL, motivo);
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_NOMBRE_PANTALLA + " | " + pantallaDondeSeEjecutoEvento);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SURVEY_ANALTICS_ACTION_PANTALLA_SELECTION_BUTTON, extras, properties);
        }

        public static void onClickBotonConfirmar2(String motivo, String pantallaDondeSeEjecutoEvento, User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_SB);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.SURVEY_ANALTICS_CALIFICACION_CLIC_EN_CONFIRMAR);
            extras.putString(GlobalConstant.EVENT_VAR_LABEL, motivo);
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_NOMBRE_PANTALLA + pantallaDondeSeEjecutoEvento);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SURVEY_ANALTICS_ACTION_PANTALLA_SELECTION_BUTTON, extras, properties);
        }

        public static void trackScreenTipoCalificacion(String tipoCalificacion, User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SURVEY_ANALTICS_CALIFICACION_NOMBRE_PANTALLA + tipoCalificacion);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SURVEY_ANALTICS_ACTION_PANTALLA_VISTA, extras, properties);
        }

        public static void trackClickTipoCalificacion(String calificacionActual, String calificacionAnterior, User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_SB);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.SURVEY_CLICK_CALIFICACION);
            extras.putString(GlobalConstant.EVENT_VAR_LABEL, calificacionActual);
            if(calificacionAnterior.trim().length() > 0) {
                extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_NOMBRE_PANTALLA + " | " + calificacionAnterior);
            }else {
                extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_NOMBRE_PANTALLA);
            }

            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SURVEY_ANALTICS_ACTION_PANTALLA_SELECTION_BUTTON, extras, properties);
        }

        public static void clickEncuesta(String camp, LoginModel user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SURVEY_ANALTICS_CALIFICACION_MIS_PEDIDOS);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.SURVEY_ANALTICS_CALIFICACION_IR_ENCUESTA);
            extras.putString(GlobalConstant.EVENT_VAR_LABEL, camp);
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SURVEY_ANALTICS_CALIFICACION_MIS_PEDIDOS);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.SURVEY_ANALTICS_ENCUESTA_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SURVEY_ANALTICS_ACTION_PANTALLA_SELECTION_BUTTON, extras, properties);
        }
    }

    public static class InformacionCampanias{

        public static void pantallaVista(String nombre_pantalla, User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.INFO_CAMPANIAS_PANTALLA_VISTA + " | " + nombre_pantalla);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.INFO_CAMPANIAS_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.INFO_CAMPANIAS_ACTION_PANTALLA_VISTA, extras, properties);
        }

        public static void onClickMenuLateral(User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.INFO_CAMPANIAS_MENU_LATERAL);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.INFO_CAMPANIAS_ACTION_MENU_LATERAL);
            extras.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.INFO_CAMPANIAS_LABEL_MENU_LATERAL);
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.INFO_CAMPANIAS_NOMBRE_PANTALLA);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.INFO_CAMPANIAS_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.INFO_CAMPANIAS_ACTION_MENU_LATERAL_NAVIGATION, extras, properties);
        }

        public static void OnClickTab(String currentTabTitle, String passTabTitle, User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.INFO_CAMPANIAS_PANTALLA_VISTA);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.INFO_CAMPANIAS_ACTION_TAB);
            extras.putString(GlobalConstant.EVENT_VAR_LABEL, currentTabTitle);
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.INFO_CAMPANIAS_PANTALLA_VISTA + " | " + passTabTitle);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.INFO_CAMPANIAS_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.INFO_CAMPANIAS_ACTION_TAB_NAVIGATION, extras, properties);
        }
    }

    public static class TuVozOnline{

        public static void OnClickMenuLateral(User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.TU_VOZ_ONLINE_CATEGORY);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.TU_VOZ_ONLINE_ACTION);
            extras.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.TU_VOZ_ONLINE_LABEL);
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.TU_VOZ_ONLINE_SCREEN_NAME);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.TU_VOZ_ONLINE_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.TU_VOZ_ONLINE_MENU_LATERAL_ACTION, extras, properties);
        }

    }

    public static class EscanerQR{

        public static void OnClickMenuLateral(User user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCANNER_QR_MENU_LATERAL_CATEGORY);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.SCANNER_QR_MENU_LATERAL_ACTION);
            extras.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.SCANNER_QR_MENU_LATERAL_LABEL);
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCANNER_QR_MENU_LATERAL_SCREEN_NAME);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.SCANNER_QR_MENU_LATERAL_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SCANNER_QR_MENU_LATERAL_EVENT, extras, properties);
        }

    }

    public static class ConferenciaDigital{

        public static void PantallaVista(LoginModel user){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.CONFERENCIA_DIGITAL_SCREEN_NAME);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.CONFERENCIA_DIGITAL_AMBIENTE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.CONFERENCIA_DIGITAL_SCREEN_VIEWS, extras, properties);
        }

        public static void InicioFinVideo(LoginModel user, Boolean iniciarVideo){
            Bundle extras = new Bundle();
            extras.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.CONFERENCIA_DIGITAL_SCREEN_NAME);
            extras.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.CONFERENCIA_DIGITAL_VIDEO);
            extras.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.CONFERENCIA_DIGITAL_SCREEN_NAME);
            extras.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.CONFERENCIA_DIGITAL_AMBIENTE);

            if(iniciarVideo){
                extras.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.CONFERENCIA_DIGITAL_ACTION_START_VIDEO);
            }else {
                extras.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.CONFERENCIA_DIGITAL_ACTION_END_VIDEO);
            }

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.CONFERENCIA_DIGITAL_ACTION_VIRTUAL_EVENT, extras, properties);
        }

    }

    public static class Gallery{

        public static void trackScreen(User user) {
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_SCREEN_NAME);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.BUILD_TYPE);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.SCREEN_VIEW, bundle, properties);
        }

        public static void trackMenuLateral(User user) {
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.GALLERY_MENU_LATERAL);
            bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.GALLERY_ACTION);
            bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.GALLERY_LABEL);
            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_SCREEN_NAME_HOME);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.GALLERY_ENVIRONTMENT);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.GALLERY_MENU_LATERAL_NAVIGATION, bundle, properties);
        }

        public static void trackBtnFiltrosClic(User user){
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.GALLERY_DECORA_TU_NAVIDAD);
            bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.GALLERY_CLICK_FILTRO);
            bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.GALLERY_NOT_AVAILABLE);
            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_SCREEN_NAME);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.GALLERY_ENVIRONTMENT);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, bundle, properties);
        }

        public static void trackFiltrar(String tipoFIltro, String nameFIltro, User user){
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.GALLERY_DECORA_TU_NAVIDAD);
            bundle.putString(GlobalConstant.EVENT_VAR_ACTION, tipoFIltro);
            bundle.putString(GlobalConstant.EVENT_VAR_LABEL, nameFIltro);
            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_SCREEN_NAME);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.GALLERY_ENVIRONTMENT);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, bundle, properties);
        }

        public static void trackAplicarFiltrar(String filtrosSeleccionados, User user){
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.GALLERY_DECORA_TU_NAVIDAD);
            bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.GALLERY_APLICAR_FILTROS);
            bundle.putString(GlobalConstant.EVENT_VAR_LABEL, filtrosSeleccionados);
            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_SCREEN_NAME);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.GALLERY_ENVIRONTMENT);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, bundle, properties);
        }

        public static void trackLimpiarFiltrar(User user){
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.GALLERY_DECORA_TU_NAVIDAD);
            bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.GALLERY_LIMPIAR_FILTROS);
            bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.GALLERY_NOT_AVAILABLE);
            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_SCREEN_NAME);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.GALLERY_ENVIRONTMENT);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, bundle, properties);
        }

        public static void trackImageClic(String imageName, User user){
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.GALLERY_DECORA_TU_NAVIDAD);
            bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.GALLERY_CLICK_IMAGEN);
            bundle.putString(GlobalConstant.EVENT_VAR_LABEL, imageName);
            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_TAB_NAME);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.GALLERY_ENVIRONTMENT);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, bundle, properties);
        }

        public static void trackSaveImage(String action, String imageName, User user){
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.GALLERY_DECORA_TU_NAVIDAD);
            bundle.putString(GlobalConstant.EVENT_VAR_ACTION, action);
            bundle.putString(GlobalConstant.EVENT_VAR_LABEL, imageName);
            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_NAME_POP_UP);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.GALLERY_ENVIRONTMENT);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, bundle, properties);
        }

        public static void trackVolverIntentar(String btnName, User user){
            if(user == null) return;

            Bundle bundle = new Bundle();

            bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.GALLERY_DECORA_TU_NAVIDAD);
            bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.GALLERY_CLICK_BOTON);
            bundle.putString(GlobalConstant.EVENT_VAR_LABEL, btnName);
            bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.GALLERY_NAME_SIN_CONEXION);
            bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, GlobalConstant.GALLERY_ENVIRONTMENT);

            Bundle properties = AnalyticsUtil.getUserProperties(user);

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, bundle, properties);
        }
    }
}
