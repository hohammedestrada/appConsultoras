package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({
    MenuCode.HOME,
    MenuCode.INCENTIVES,
    MenuCode.DEBTS,
    MenuCode.ORDERS,
    MenuCode.ORDERS_NATIVE,
    MenuCode.CLIENTS_WITH_ORDER,
    MenuCode.CLIENTS,
    MenuCode.CONFIGURATION,
    MenuCode.TUTORIAL,
    MenuCode.BUZON,
    MenuCode.TERMINOS,
    MenuCode.PRODUCTOS_AGOTADOS,
    MenuCode.ESTADO_CUENTA,
    MenuCode.CLOSE,
    MenuCode.CAMBIOS_DEVOLUCIONES,
    MenuCode.PEDIDOS_FIC,
    MenuCode.MIS_PEDIDOS,
    MenuCode.MI_NEGOCIO,
    MenuCode.ACADEMIA,
    MenuCode.CAMINO,
    MenuCode.TUVOZ,
    MenuCode.CHATBOT,
    MenuCode.OTHERS,
    MenuCode.MQVIRTUAL,
    MenuCode.PEDIDOPEND,
    MenuCode.CAMINO_BRILLANTE,
    MenuCode.TERMOMETRO_SUENIO,
    MenuCode.ESCANER_QR,
    MenuCode.NAVI_FEST,
    MenuCode.ASESOR_REGALO
})
public @interface MenuCode {
    String HOME = "MEN_LAT_INICIO";
    String INCENTIVES = "MEN_LAT_INCENTIVOS";
    String DEBTS = "MEN_LAT_DEUDAS";
    String ORDERS = "MEN_LAT_PEDIDO";
    String ORDERS_NATIVE = "MEN_LAT_PEDIDOSNAT";
    String CLIENTS = "MEN_LAT_CLIENTES";
    String CLIENTS_WITH_ORDER = "MEN_LAT_MISCLIENTES_CONPEDIDO";
    String CONFIGURATION = "MEN_CONFIGURACION";
    String CATALOG = "MEN_LAT_CATALOGOS";
    String TUTORIAL = "MEN_LAT_TUTORIAL";
    String BUZON = "MEN_LAT_BUZONSUGER";
    String TERMINOS = "MEN_LAT_TERMCOND";
    String PRODUCTOS_AGOTADOS = "MEN_LAT_PRODAGOTADO";
    String ESTADO_CUENTA = "MEN_LAT_ESTCUENTA";
    String TRACK_ORDERS = "MEN_LAT_SEGPEDIDO";
    String CLOSE = "MEN_CLOSE";
    String OFFERS = "MEN_LAT_OFERTAS";
    String CLOSEOUT = "MEN_LAT_LIQWEB";
    String CAMBIOS_DEVOLUCIONES = "MEN_LAT_CAMBDEV";
    String PEDIDOS_FIC = "MEN_LAT_PEDIDOFIC";
    String MIS_PEDIDOS = "MEN_LAT_MISPEDIDOS";
    String MI_NEGOCIO = "MEN_LAT_NEGOCIO";
    String ACADEMIA = "MEN_LAT_MIACADEMIA";
    String CAMINO = "MEN_LAT_CAM_EXITO";
    String TUVOZ = "MEN_LAT_TUVOZ";
    String CHATBOT = "MEN_LAT_CHAT_BOT";
    String OTHERS = "MEN_LAT_OTRASAPP";
    String MQVIRTUAL = "MEN_LAT_MAQVIR";
    String PEDIDOPEND = "MEN_LAT_PEDIDOPEND";
    String CAMINO_BRILLANTE = "MEN_LAT_CAMINOBRILLANTE";
    String ESCANER_QR = "MEN_LAT_ESCANER";
    String INFO_CAMPANIA = "MEN_LAT_INFO_CAMP";
    String MEN_LAT_SEC_DESCARGA = "MEN_LAT_SEC_DESCARGA";
    String CONFERENCIA_DIGITAL = "MEN_CONFERENCIA_DIGITAL";
    String TERMOMETRO_SUENIO = "MEN_LAT_MEDIDORSUEINO";
    String NAVI_FEST = "MEN_LAT_NAVIFEST";
    String ASESOR_REGALO = "MEN_LAT_ASESOR_REG";
    String MEN_ESIKA_AHORA = "MEN_LAT_ESIKA_AHORA";
}
