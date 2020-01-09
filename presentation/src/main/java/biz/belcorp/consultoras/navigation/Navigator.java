package biz.belcorp.consultoras.navigation;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.feature.auth.login.LoginActivity;
import biz.belcorp.consultoras.feature.auth.recovery.RecoveryActivity;
import biz.belcorp.consultoras.feature.auth.registration.RegistrationActivity;
import biz.belcorp.consultoras.feature.auth.startTutorial.ContenidoResumenDetalleModel;
import biz.belcorp.consultoras.feature.auth.startTutorial.ContenidoResumenModel;
import biz.belcorp.consultoras.feature.client.registration.ClientRegistrationActivity;
import biz.belcorp.consultoras.feature.config.ConfigActivity;
import biz.belcorp.consultoras.feature.contest.constancy.PerConstancyActivity;
import biz.belcorp.consultoras.feature.contest.news.NewProgramActivity;
import biz.belcorp.consultoras.feature.contest.order.current.PerCurrentOrderActivity;
import biz.belcorp.consultoras.feature.datami.DatamiMessageActivity;
import biz.belcorp.consultoras.feature.debt.AddDebtActivity;
import biz.belcorp.consultoras.feature.embedded.academia.AcademiaActivity;
import biz.belcorp.consultoras.feature.embedded.esikaahora.EsikaAhoraWebActivity;
import biz.belcorp.consultoras.feature.embedded.gpr.OrderWebActivity;
import biz.belcorp.consultoras.feature.embedded.offers.OffersActivity;
import biz.belcorp.consultoras.feature.embedded.pedidospendientes.PedidosPendientesActivity;
import biz.belcorp.consultoras.feature.embedded.tuvoz.TuVozOnlineWebActivity;
import biz.belcorp.consultoras.feature.ficha.caminobrillante.FichaCaminoBrillanteActivity;
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity;
import biz.belcorp.consultoras.feature.ficha.ofertafinal.FichaOfertaFinalActivity;
import biz.belcorp.consultoras.feature.ficha.premio.FichaPremioActivity;
import biz.belcorp.consultoras.feature.ficha.producto.FichaProductoActivity;
import biz.belcorp.consultoras.feature.home.HomeActivity;
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity;
import biz.belcorp.consultoras.feature.home.cupon.CuponActivity;
import biz.belcorp.consultoras.feature.home.fest.FestActivity;
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackActivity;
import biz.belcorp.consultoras.feature.home.subcampaing.FestSubCampaingActivity;
import biz.belcorp.consultoras.feature.home.tutorial.TutorialActivity;
import biz.belcorp.consultoras.feature.home.tutorial.TutorialFragment;
import biz.belcorp.consultoras.feature.legal.LegalActivity;
import biz.belcorp.consultoras.feature.notifications.list.NotificationListActivity;
import biz.belcorp.consultoras.feature.ofertafinal.OfertaFinalActivity;
import biz.belcorp.consultoras.feature.scanner.ScannerActivity;
import biz.belcorp.consultoras.feature.search.list.SearchListActivity;
import biz.belcorp.consultoras.feature.stockouts.StockoutsActivity;
import biz.belcorp.consultoras.feature.terms.TermsActivity;
import biz.belcorp.consultoras.feature.welcome.WelcomeActivity;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import biz.belcorp.library.log.BelcorpLogger;

@Singleton
public class Navigator {

    public static final String NAVIGATE_FROM_LOGIN = "navigate_from_login";
    public static final String NAVIGATE_FROM_LOGIN_FORM = "navigate_from_login_form";
    public static final String NAVIGATE_FROM_LOGIN_FACEBOOK = "navigate_from_login_facebook";

    public static final int REQUEST_CODE_TERMS = 100;
    public static final int REQUEST_CODE_TUTORIAL = 101;
    public static final int REQUEST_CODE_FICHA_PREMIO = 102;
    public static final int REQUEST_CODE_LANDING_OFERTAFINAL = 103;

    @Inject
    Navigator() {
        //empty
    }

    public void navigateToLogin(Context context, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = LoginActivity.Companion.getCallingIntent(context);
            intentToLaunch.putExtra(NAVIGATE_FROM_LOGIN, true);
            if (null != extras) intentToLaunch.putExtras(extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToLoginForm(Context context) {
        if (context != null) {
            Intent intentToLaunch = LoginActivity.Companion.getCallingIntent(context);
            intentToLaunch.putExtra(NAVIGATE_FROM_LOGIN_FORM, true);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToLoginFacebook(Context context, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = LoginActivity.Companion.getCallingIntent(context);
            intentToLaunch.putExtra(NAVIGATE_FROM_LOGIN_FACEBOOK, true);
            if (null != extras) intentToLaunch.putExtras(extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToHome(Context context, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = HomeActivity.Companion.getCallingIntent(context);
            if (null != extras) intentToLaunch.putExtras(extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToRegistration(Context context, FacebookProfileModel facebookProfile, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = RegistrationActivity.Companion.getCallingIntent(context);
            extras.putParcelable("facebook", facebookProfile);
            intentToLaunch.putExtras(extras);
            intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToRecovery(Context context) {
        if (context != null) {
            Intent intentToLaunch = RecoveryActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void openIntentLink(Context context, String url) {
        if (context != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            try {
                context.startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                BelcorpLogger.w("openIntentLink", e);
            }
        }
    }

    public void navigateToConfiguration(Context context) {
        if (context != null) {
            Intent intentToLaunch = ConfigActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToRegisterNewClient(Context context) {
        if (context != null) {
            Intent intentToLaunch = ClientRegistrationActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToTermsWithResult(Activity activity, Bundle extras) {
        if (activity != null) {
            Intent intentToLaunch = new Intent(activity, TermsActivity.class);
            if (null != extras) intentToLaunch.putExtras(extras);
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_TERMS);
        }
    }

    public void navigateToTerms(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = new Intent(activity, TermsActivity.class);
            activity.startActivity(intentToLaunch);
        }
    }

    public void navigateToContestPerConstancy(Context context, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = PerConstancyActivity.getCallingIntent(context);
            intentToLaunch.putExtras(extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToContestPerOrder(Context context, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = PerCurrentOrderActivity.getCallingIntent(context);
            intentToLaunch.putExtras(extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToContestNewProgram(Context context, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = NewProgramActivity.getCallingIntent(context);
            intentToLaunch.putExtras(extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToTutorial(Activity activity, String consultantCode, String countryISO) {
        Intent intent = new Intent(activity, TutorialActivity.class);

        if (consultantCode != null)
            intent.putExtra(TutorialFragment.USERCODE, consultantCode);
        if (countryISO != null)
            intent.putExtra(TutorialFragment.PAIS, countryISO);

        activity.startActivity(intent);
    }

    public void navigateToTutorialWithResult(Activity activity, String consultantCode, String countryISO) {
        Intent intent = new Intent(activity, TutorialActivity.class);

        if (consultantCode != null)
            intent.putExtra(TutorialFragment.USERCODE, consultantCode);
        if (countryISO != null)
            intent.putExtra(TutorialFragment.PAIS, countryISO);

        activity.startActivityForResult(intent, REQUEST_CODE_TUTORIAL);
    }

    public void navigateToAddDebt(Context context) {
        if (context != null) {
            Intent intentToLaunch = AddDebtActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToStockouts(Context context) {
        if (context != null) {
            Intent intentToLaunch = StockoutsActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToLegal(Context context) {
        if (context != null) {
            Intent intentToLaunch = LegalActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToOffers(Context context, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = OffersActivity.getCallingIntent(context);
            intentToLaunch.putExtras(extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToDatamiMessage(Context context, Bundle extras) {
        if (context != null) {
            Intent intentToLaunch = DatamiMessageActivity.Companion.getCallingIntent(context);
            if (null != extras) intentToLaunch.putExtras(extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToAcademia(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = new Intent(activity, AcademiaActivity.class);
            activity.startActivity(intentToLaunch);
        }
    }

    public void navigateToWelcome(Activity activity, Bundle extras) {
        if (activity != null) {
            Intent intentToLaunch = new Intent(activity, WelcomeActivity.class);
            intentToLaunch.putExtras(extras);
            intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            activity.startActivity(intentToLaunch);
        }
    }

    public void navigateToTuVozOnline(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = new Intent(activity, TuVozOnlineWebActivity.class);
            activity.startActivity(intentToLaunch);
        }
    }

    public void navigateToSearch(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = new Intent(activity, SearchListActivity.class);
            activity.startActivity(intentToLaunch);
        }
    }

    public void navigateToNotificationList(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = new Intent(activity, NotificationListActivity.class);
            activity.startActivity(intentToLaunch);
        }
    }

    public void navigateToSearchWithResult(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, SearchListActivity.class);
            activity.startActivityForResult(intent, SearchListActivity.RESULT);
        }
    }

    public void navigateToAddOrders(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, AddOrdersActivity.class);
            activity.startActivity(intent);
        }
    }

    public void navigateToAddOrdersWithResult(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, AddOrdersActivity.class);
            activity.startActivityForResult(intent, AddOrdersActivity.RESULT);
        }
    }

    public void navigateToAddOrdersFromAtpWithResult(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, AddOrdersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity.startActivityForResult(intent, AddOrdersActivity.RESULT);
        }
    }

    public void navigateToOrdersWithResult(Activity activity, Menu menu) {
        if (activity != null) {
            if (menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS)) {
                Intent intent = new Intent(activity, OrderWebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivityForResult(intent, OrderWebActivity.RESULT);
            } else if (menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS_NATIVE)) {
                Intent intent = new Intent(activity, AddOrdersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivityForResult(intent, AddOrdersActivity.RESULT);
            }
        }
    }

    public void navigateToOrders(Activity activity, Menu menu) {
        if (activity != null) {
            if (menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS)) {
                Intent intent = new Intent(activity, OrderWebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            } else if (menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS_NATIVE)) {
                Intent intent = new Intent(activity, AddOrdersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        }
    }

    public void navigateToCupon(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, CuponActivity.class);
            activity.startActivity(intent);
        }
    }

    public void navigateToPedidosPendiente(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, PedidosPendientesActivity.class);
            activity.startActivityForResult(intent, PedidosPendientesActivity.RESULT);
        }
    }

    public void navigateToFicha(Context context, Bundle extras) {

        if (context != null) {
            Intent intentToLaunch = FichaProductoActivity.Companion.getCallingIntent(context, extras);
            context.startActivity(intentToLaunch);
        }

    }

    public void navigateToFichaOfertaFinal(Context context, Bundle extras) {

        if (context != null) {
            Intent intentToLaunch = FichaOfertaFinalActivity.Companion.getCallingIntent(context, extras);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToFichaCaminoBrillante(Activity activity, Bundle extras) {

        if (activity != null) {
            Intent intentToLaunch = FichaCaminoBrillanteActivity.Companion.getCallingIntent(activity, extras);
            activity.startActivityForResult(intentToLaunch, BaseFichaActivity.RESULT);
        }

    }

    public void navigateToArmaTuPack(Context context, Bundle extras) {
        if (context != null) {
            Intent intent = ArmaTuPackActivity.Companion.getCallingIntent(context);
            if (null != extras) {
                intent.putExtras(extras);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            context.startActivity(intent);
        }
    }


    public void navigateToFest(Context context, Bundle extras) {
        if (context != null) {
            Intent intent = FestActivity.Companion.getCallingIntent(context);
            if (null != extras) {
                intent.putExtras(extras);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            context.startActivity(intent);
        }
    }

    public void navigateToFichaProductWithResult(Activity activity, Bundle extras) {
        if (activity != null) {
            Intent intentToLaunch = FichaProductoActivity.Companion.getCallingIntent(activity, extras);
            activity.startActivityForResult(intentToLaunch, BaseFichaActivity.RESULT);
        }
    }

    public void navigateToScannerQR(Context context, Bundle extras){
        if (context != null) {
            Intent intent = ScannerActivity.Companion.getCallingIntent(context);
            if (null != extras) intent.putExtras(extras);
            context.startActivity(intent);
        }
    }

    public void navigateToFichaPremio(Activity activity, Bundle extras) {
        Intent intent = new Intent(activity, FichaPremioActivity.class);
        intent.putExtras(extras);
        activity.startActivityForResult(intent, REQUEST_CODE_FICHA_PREMIO);
    }

    public void navigateToEsikaAhora(Context context, Bundle extras){
        if (context != null) {
            Intent intent = EsikaAhoraWebActivity.Companion.getCallingIntent(context);
            if (null != extras) intent.putExtras(extras);
            context.startActivity(intent);
        }
    }

    public void navigateToOfertaFinalLandig(Activity activity, Bundle extras){
        if (activity != null) {
            Intent intent = new Intent(activity, OfertaFinalActivity.class);
            if (null != extras) intent.putExtras(extras);
            activity.startActivityForResult(intent, REQUEST_CODE_LANDING_OFERTAFINAL);
        }
    }

    public void navigateToSubcampaign(Context context, Bundle extras){
        if (context != null) {
            Intent intent = FestSubCampaingActivity.Companion.getCallingIntent(context);
            if (null != extras) intent.putExtras(extras);
            context.startActivity(intent);
        }
    }
}
