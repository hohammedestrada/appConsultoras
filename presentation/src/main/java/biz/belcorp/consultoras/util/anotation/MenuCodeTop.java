package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    MenuCodeTop.HOME,
    MenuCodeTop.INCENTIVES,
    MenuCodeTop.DEBTS,
    MenuCodeTop.ORDERS,
    MenuCodeTop.CLIENTS,
    MenuCodeTop.CONFIGURATION,
    MenuCodeTop.CATALOG,
    MenuCodeTop.OFFERS,
    MenuCodeTop.STOCKOUT,
    MenuCodeTop.ACCOUNT_STATE,
    MenuCodeTop.TRACK_ORDERS,
    MenuCodeTop.ORDERS_NATIVE,
    MenuCodeTop.TUVOZ,
    MenuCodeTop.CHATBOT
})
public @interface  MenuCodeTop {
    String HOME = "MEN_TOP_INICIO";
    String INCENTIVES = "MEN_TOP_INCENTIVOS";
    String DEBTS = "MEN_TOP_DEUDAS";
    String ORDERS = "MEN_TOP_PEDIDO";
    String CLIENTS = "MEN_TOP_CLIENTES";
    String CONFIGURATION = "MEN_TOP_CONFIGURACION";
    String CATALOG = "MEN_TOP_CATALOGOS";
    String OFFERS = "MEN_TOP_OFERTAS";
    String STOCKOUT = "MEN_TOP_PRODAGOTADO";
    String ACCOUNT_STATE = "MEN_TOP_ESTCUENTA";
    String TRACK_ORDERS = "MEN_TOP_SEGPEDIDO";
    String ORDERS_NATIVE = "MEN_TOP_PEDIDOSNAT";
    String TUVOZ = "MEN_TOP_TUVOZ";
    String CHATBOT = "MEN_TOP_CHAT_BOT";
}
