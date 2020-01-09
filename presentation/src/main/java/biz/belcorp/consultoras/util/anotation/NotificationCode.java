package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    NotificationCode.HOME,
    NotificationCode.HOME_ANIM_50,
    NotificationCode.ORDER,
    NotificationCode.OFFERS,
    NotificationCode.OFFERS_SHOWROOM,
    NotificationCode.OFFERS_NEW_NEW,
    NotificationCode.OFFERS_FOR_YOU,
    NotificationCode.OFFERS_ONLY_TODAY,
    NotificationCode.OFFERS_SALES_TOOLS,
    NotificationCode.OFFERS_INFORMATION,
    NotificationCode.OFFERS_THE_MOST_WINNING,
    NotificationCode.OFFERS_PERFECT_DUO,
    NotificationCode.CATALOG,
    NotificationCode.ACCOUNT,
    NotificationCode.ANNOUNCEMENT,
    NotificationCode.MY_ACADEMY,
    NotificationCode.MY_PROFILE,
    NotificationCode.STOCKOUTS,
    NotificationCode.CHANGE,
    NotificationCode.ORDERS_FIC,
    NotificationCode.MY_ORDER,
    NotificationCode.CLOSE_OUT,
    NotificationCode.ASESOR_DE_BELLEZA,

    NotificationCode.ORDER_EMOJI,
    NotificationCode.ORDER_TWO_EMOJI,
    NotificationCode.ORDER_THREE_EMOJI,
    NotificationCode.OFFERS_EMOJI,
    NotificationCode.OFFERS_TWO_EMOJI,
    NotificationCode.OFFERS_THREE_EMOJI,
    NotificationCode.OFFERS_FOUR_EMOJI,
    NotificationCode.OFFERS_FIVE_EMOJI,
    NotificationCode.OFFERS_SIX_EMOJI,
    NotificationCode.OFFERS_SEVEN_EMOJI,
    NotificationCode.OFFERS_EIGHT_EMOJI,
    NotificationCode.OFFERS_NINE_EMOJI,
    NotificationCode.OFFERS_SHOWROOM_EMOJI,
    NotificationCode.OFFERS_SHOWROOM_TWO_EMOJI,
    NotificationCode.OFFERS_NEW_NEW_EMOJI,
    NotificationCode.OFFERS_FOR_YOU_EMOJI,
    NotificationCode.OFFERS_ONLY_TODAY_EMOJI,
    NotificationCode.OFFERS_SALES_TOOLS_EMOJI,
    NotificationCode.OFFERS_INFORMATION_EMOJI,
    NotificationCode.CATALOG_EMOJI,
    NotificationCode.CATALOG_TWO_EMOJI,
    NotificationCode.ACCOUNT_EMOJI,
    NotificationCode.ACCOUNT_TWO_EMOJI,
    NotificationCode.HOME_ANIM_50_EMOJI,
    NotificationCode.ANNOUNCEMENT_EMOJI,
    NotificationCode.ACADEMY_EMOJI,
    NotificationCode.ACADEMY_TWO_EMOJI,
    NotificationCode.ACADEMY_HOME_EMOJI,
    NotificationCode.MY_PROFILE_ONE_EMOJI,
    NotificationCode.MY_PROFILE_TWO_EMOJI,
    NotificationCode.MY_PROFILE_THREE_EMOJI,
    NotificationCode.MY_PROFILE_FOUR_EMOJI,
    NotificationCode.STOCKOUTS_EMOJI,
    NotificationCode.CHANGE_EMOJI,
    NotificationCode.ORDERS_FIC_EMOJI,
    NotificationCode.MY_ORDER_ONE_EMOJI,
    NotificationCode.MY_ORDER_TWO_EMOJI,
    NotificationCode.CLOSE_OUT_EMOJI,
    NotificationCode.HUNDRED_POINTS_EMOJI,

    NotificationCode.HOME_INCENTIVES_EMOJI,
    NotificationCode.HOME_INCENTIVES_TWO_EMOJI,
    NotificationCode.HOME_INCENTIVES_THREE_EMOJI,
    NotificationCode.HOME_DEBTS_EMOJI,
    NotificationCode.HOME_DEBTS_TWO_EMOJI,
    NotificationCode.HOME_CLIENTS_EMOJI,
    NotificationCode.HOME_CLIENTS_TWO_EMOJI,

    NotificationCode.PAYONLINE_EMOJI,
    NotificationCode.PAYONLINE_TWO_EMOJI,
    NotificationCode.ORDER_SCALE_EMOJI,
    NotificationCode.ASESOR_DE_BELLEZA_EMOJI,
    NotificationCode.CONFERENCIA_DIGITAL_EMOJI,
    NotificationCode.CONFERENCIA_DIGITAL
})
public @interface NotificationCode {

    //String DEFAULT_EMOJI = "U+1F646";
    String ORDER_EMOJI = "U+1F4DD";
    String ORDER_TWO_EMOJI = "U+1F614";
    String ORDER_THREE_EMOJI = "U+23F0";
    String OFFERS_EMOJI = "U+1F389";
    String OFFERS_TWO_EMOJI = "U+1F4B0";
    String OFFERS_THREE_EMOJI = "U+1F64B";
    String OFFERS_FOUR_EMOJI = "U+1F382";
    String OFFERS_FIVE_EMOJI = "U+1F385";
    String OFFERS_SIX_EMOJI = "U+1F384";
    String OFFERS_SEVEN_EMOJI = "U+1F339";
    String OFFERS_EIGHT_EMOJI = "U+1F451";
    String OFFERS_NINE_EMOJI = "U+1F490";
    String OFFERS_SHOWROOM_EMOJI = "U+1F6A8";
    String OFFERS_SHOWROOM_TWO_EMOJI = "U+1F446";       // Ofertas Showroom Ofertas Especiales
    String OFFERS_NEW_NEW_EMOJI = "U+2B50";             // Ofertas lo Nuevo Nuevo
    String OFFERS_FOR_YOU_EMOJI = "U+1F51D";            // Ofertas para ti
    String OFFERS_ONLY_TODAY_EMOJI = "U+1F6A8";         // Ofertas solo hoy
    String OFFERS_SALES_TOOLS_EMOJI = "U+1F4E3";        // Ofertas herramientas de venta
    String OFFERS_INFORMATION_EMOJI = "U+1F64C";        // Ofertas saber más / inscripción
    String CATALOG_EMOJI = "U+1F4D5";
    String CATALOG_TWO_EMOJI = "U+261D";
    String ACCOUNT_EMOJI = "U+1F4B5";
    String ACCOUNT_TWO_EMOJI = "U+1F4C6";
    String HOME_ANIM_50_EMOJI = "U+1F3C6";
    String ANNOUNCEMENT_EMOJI = "U+1F60B";
    String ACADEMY_EMOJI = "U+1F4A1";
    String ACADEMY_TWO_EMOJI = "U+1F600";
    String ACADEMY_HOME_EMOJI = "U+1F4D6";
    String MY_PROFILE_ONE_EMOJI = "U+1F4E7";
    String MY_PROFILE_TWO_EMOJI = "U+1F4F1";
    String MY_PROFILE_THREE_EMOJI = "U+1F478";
    String MY_PROFILE_FOUR_EMOJI = "U+1F471";
    String STOCKOUTS_EMOJI = "U+1F610";
    String CHANGE_EMOJI = "U+1F514";
    String ORDERS_FIC_EMOJI = "U+1F440";
    String MY_ORDER_ONE_EMOJI = "U+1F449";
    String MY_ORDER_TWO_EMOJI = "U+1F4E6";
    String CLOSE_OUT_EMOJI = "U+23F3";

    String HOME_INCENTIVES_EMOJI = "U+1F381";
    String HOME_INCENTIVES_TWO_EMOJI = "U+1F60D";
    String HOME_INCENTIVES_THREE_EMOJI = "U+1F31F";
    String HOME_DEBTS_EMOJI = "U+1F45B";
    String HOME_DEBTS_TWO_EMOJI = "U+1F44F";
    String HOME_CLIENTS_EMOJI = "U+1F469";
    String HOME_CLIENTS_TWO_EMOJI = "U+1F468";

    String PAYONLINE_EMOJI = "U+1F4B3";
    String PAYONLINE_TWO_EMOJI = "U+1F38A";
    String ORDER_SCALE_EMOJI = "U+1F4AA";
    String HUNDRED_POINTS_EMOJI = "U+1F4AF";

    String THE_MOST_WINNING_EMOJI = "U+1F4B8";
    String PERFECT_DUO_EMOJI = "U+1F49D";

    String CAMINO_BRILLANTE_LANDING_EMOJI = "U+2728";
    String CAMINO_BRILLANTE_OFERTAS_EMOJI = "U+1F680";
    String CAMINO_BRILLANTE_MEDALLAS_EMOJI = "U+1F3C6";

    String DREAM_METER_LANDING_EMOJI = "U+1F320";


    String ASESOR_DE_BELLEZA_EMOJI = "U+1F484";

    String ARMA_TU_PACK_EMOJI = "U+1F4A0";

    String TU_VOZ_ONLINE_EMOJI = "U+1F4F2";

    String CALENDARIO_EMOJI = "U+1F4C5";

    String CONFERENCIA_DIGITAL_EMOJI = "U+1F4F9";
    String FEST_LANDING_EMOJI = "U+1F386";
    String SUB_CAMPAIGN_EMOJI = "U+1F4AB";
    String ESIKA_AHORA_EMOJI = "U+1F4D1";

    String HOME = "HOME_ACTIVITY";
    String HOME_ANIM_50 = "HOME_ANIM_50_ACTIVITY";
    String ORDER = "ORDER_ACTIVITY";
    String OFFERS = "OFFERS_ACTIVITY";
    String OFFERS_SHOWROOM = "OFFERS_SHOWROOM_ACTIVITY";
    String OFFERS_NEW_NEW = "OFFERS_NEW_NEW_ACTIVITY";
    String OFFERS_FOR_YOU = "OFFERS_FOR_YOU_ACTIVITY";
    String OFFERS_ONLY_TODAY = "OFFERS_ONLY_TODAY_ACTIVITY";
    String OFFERS_SALES_TOOLS = "OFFERS_SALES_TOOLS_ACTIVITY";
    String OFFERS_INFORMATION = "OFFERS_INFORMATION_ACTIVITY";
    String OFFERS_THE_MOST_WINNING = "OFFERS_THE_MOST_WINNING_ACTIVITY";
    String OFFERS_PERFECT_DUO = "OFFERS_PERFECT_DUO_ACTIVITY";
    String CATALOG = "CATALOG_ACTIVITY";
    String ACCOUNT = "ACCOUNT_ACTIVITY";
    String ANNOUNCEMENT = "ANNOUNCEMENT_ACTIVITY";
    String MY_ACADEMY = "ACADEMY_ACTIVITY";
    String MY_PROFILE = "MY_PROFILE_ACTIVITY";
    String STOCKOUTS = "STOCKOUTS_ACTIVITY";
    String CHANGE = "CHANGE_ACTIVITY";
    String ORDERS_FIC = "ORDERSFIC_ACTIVITY";
    String MY_ORDER = "MY_ORDER_ACTIVITY";
    String CLOSE_OUT = "CLOSE_OUT_ACTIITY";
    String CAMINO_BRILLANTE_LANDING = "CAMINO_BRILLANTE_LANDING_ACTIVITY";
    String CAMINO_BRILLANTE_OFERTAS_ESPECIALES = "CAMINO_BRILLANTE_OFERTAS_ESPECIALES_ACTIVITY";
    String CAMINO_BRILLANTE_MEDALLAS = "CAMINO_BRILLANTE_MEDALLAS_ACTIVITY";
    String INFORMACION_CAMPANIAS = "CAMPAIGN_INFORMATION_ACTIVITY";
    String ASESOR_DE_BELLEZA = "ASESOR_DE_BELLEZA_ACTIVITY";
    String ARMA_TU_PACK = "ARMA_TU_PACK_ACTIVITY";
    String TU_VOZ_ONLINE = "TU_VOZ_ONLINE_ACTIVITY";
    String CALENDARIO_FACTURACION = "CALENDARIO_FACTURACION_ACTIVITY";
    String CONFERENCIA_DIGITAL = "CONFERENCIA_DIGITAL_ACTIVITY";
    String DREAM_METER_LANDING = "DREAM_METER_LANDING_ACTIVITY";
    String FEST_LANDING = "FEST_LANDING_ACTIVITY";
    String ESIKA_AHORA = "ESIKA_AHORA_ACTIVITY";
    String SUB_CAMPAIGN_LANDING = "SUB_CAMPAIGN_LANDING";

}
