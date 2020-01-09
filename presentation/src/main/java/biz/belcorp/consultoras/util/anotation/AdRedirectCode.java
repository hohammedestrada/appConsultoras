package biz.belcorp.consultoras.util.anotation;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    AdRedirectCode.ORDER,
    AdRedirectCode.OFFERS,
    AdRedirectCode.OFFERS_FOR_YOU,
    AdRedirectCode.OFFERS_ONLY_TODAY,
    AdRedirectCode.OFFERS_SALES_TOOLS,
    AdRedirectCode.OFFERS_INFORMATION,
    AdRedirectCode.CATALOG,
    AdRedirectCode.ACCOUNT,
    AdRedirectCode.ANNOUNCEMENT,
    AdRedirectCode.MY_PROFILE,
    AdRedirectCode.STOCKOUTS,
    AdRedirectCode.CHANGE,
    AdRedirectCode.ORDERS_FIC,
    AdRedirectCode.MY_ORDER,
    AdRedirectCode.CLOSE_OUT,
    AdRedirectCode.BONIFICACION,
    AdRedirectCode.CLIENTES,
    AdRedirectCode.PASE_PEDIDO,
    AdRedirectCode.MI_ACADEMIA,
    AdRedirectCode.TVO,
    AdRedirectCode.ACTUALIZACION_DATO,
    AdRedirectCode.GANA_MAS,
    AdRedirectCode.GANA_MAS_ODD,
    AdRedirectCode.GANA_MAS_SR,
    AdRedirectCode.GANA_MAS_MG,
    AdRedirectCode.GANA_MAS_OPT,
    AdRedirectCode.GANA_MAS_RD,
    AdRedirectCode.GANA_MAS_HV,
    AdRedirectCode.GANA_MAS_DP,
    AdRedirectCode.GANA_MAS_PN,
    AdRedirectCode.GANA_MAS_ATP,
    AdRedirectCode.GANA_MAS_LAN,
    AdRedirectCode.GANA_MAS_OPM,
    AdRedirectCode.CHAT,
    AdRedirectCode.HOME,
    AdRedirectCode.DREAM_METER_LANDING

})

public @interface AdRedirectCode {
    String ORDER = "ORDER";
    String OFFERS = "OFFERS";
    String OFFERS_FOR_YOU = "OFFERS_FOR_YOU";
    String OFFERS_ONLY_TODAY = "OFFERS_ONLY_TODAY";
    String OFFERS_SALES_TOOLS = "OFFERS_SALES_TOOLS";
    String OFFERS_INFORMATION = "OFFERS_INFORMATION";
    String CATALOG = "CATALOG";
    String ACCOUNT = "ACCOUNT";
    String ANNOUNCEMENT = "ANNOUNCEMENT";
    String MY_PROFILE = "MY_PROFILE";
    String STOCKOUTS = "STOCKOUTS";
    String CHANGE = "CHANGE";
    String ORDERS_FIC = "ORDERSFIC";
    String MY_ORDER = "MY_ORDER";
    String CLOSE_OUT = "CLOSE_OUT";
    String CAMINO_BRILLANTE_LANDING = "CBRILLANTE_LANDING";
    String CAMINO_BRILLANTE_OFERTAS_ESPECIALES = "CBRILLANTE_OFERTAS_ESP";
    String CAMINO_BRILLANTE_MEDALLAS = "CBRILLANTE_MEDALLAS";
    String BONIFICACION = RedirectionStories.BONIFICACION;
    String CLIENTES = RedirectionStories.CLIENTES;
    String PASE_PEDIDO = RedirectionStories.PASE_PEDIDO;
    String MI_ACADEMIA = RedirectionStories.MI_ACADEMIA;
    String TVO = RedirectionStories.TVO;
    String ACTUALIZACION_DATO = RedirectionStories.ACTUALIZACION_DATOS;
    String GANA_MAS = RedirectionStories.GANA_MAS;
    String GANA_MAS_ODD = RedirectionStories.GANA_MAS_ODD;
    String GANA_MAS_SR = RedirectionStories.GANA_MAS_SR;
    String GANA_MAS_MG = RedirectionStories.GANA_MAS_MG;
    String GANA_MAS_OPT = RedirectionStories.GANA_MAS_OPT;
    String GANA_MAS_RD = RedirectionStories.GANA_MAS_RD;
    String GANA_MAS_HV = RedirectionStories.GANA_MAS_HV;
    String GANA_MAS_DP = RedirectionStories.GANA_MAS_DP;
    String GANA_MAS_PN = RedirectionStories.GANA_MAS_PN;
    String GANA_MAS_ATP = RedirectionStories.GANA_MAS_ATP;
    String GANA_MAS_LAN = RedirectionStories.GANA_MAS_LAN;
    String GANA_MAS_OPM = RedirectionStories.GANA_MAS_OPM;
    String CHAT = RedirectionStories.CHAT;
    String HOME = RedirectionStories.HOME;
    String CONFERENCIA_DIGITAL = RedirectionStories.CONFERENCIA_DIGITAL;
    String DREAM_METER_LANDING = "DREAM_METER_LANDING";
    String ADD_CART_CUV = "AGR_CAR";
}
