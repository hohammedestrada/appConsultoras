package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    PageUrlType.ORDER,
    PageUrlType.ORDERDETAIL,
    PageUrlType.CLIENT_ORDER_HISTORY,
    PageUrlType.PRODUCTOS_AGOTADOS,
    PageUrlType.SHOW_ROOM,
    PageUrlType.TRACK_ORDER,
    PageUrlType.OFFERS,
    PageUrlType.GUIA_NEGOCIO,
    PageUrlType.REVISTA_DIGITAL_INFO,
    PageUrlType.THE_MOST_WINNING,
    PageUrlType.PERFECT_DUO,
    PageUrlType.LIQUIDACIONWEB,
    PageUrlType.CAMBIODEVOLUCIONES,
    PageUrlType.PEDIDOSFIC,
    PageUrlType.DETALLE_ESTRATEGIA,
    PageUrlType.PEDIDOS_PENDIENTES,
    PageUrlType.ESIKAAHORA
})
public @interface PageUrlType {
    String ORDER = "PEDIDO";
    String ORDERDETAIL = "PEDIDODETALLE";
    String CLIENT_ORDER_HISTORY = "MISPEDIDOS";
    String SHOW_ROOM = "SHOWROOM";
    String PRODUCTOS_AGOTADOS = "PRODUCTOSAGOTADOS";
    String TRACK_ORDER = "SEGUIMIENTOPEDIDO";
    String OFFERS = "OFERTAS";
    String GUIA_NEGOCIO = "GUIANEGOCIO";
    String REVISTA_DIGITAL_INFO = "REVISTADIGITALINFORMACION";
    String THE_MOST_WINNING = "MASGANADORAS";
    String PERFECT_DUO = "DUOPERFECTO";
    String LIQUIDACIONWEB = "LIQUIDACIONWEB";
    String CAMBIODEVOLUCIONES = "CAMBIODEVOLUCIONES";
    String PEDIDOSFIC = "PEDIDOSFIC";
    String DETALLE_ESTRATEGIA = "DETALLEESTRATEGIA";
    String LO_NUEVO_NUEVO = "LONUEVONUEVO";
    String OFERTAS_PARA_TI = "OFERTASPARATI";
    String SOLO_HOY = "SOLOHOY";
    String HERRAMIENTAS_DE_VENTA = "HERRAMIENTASDEVENTA";
    String PEDIDOS_PENDIENTES = "PEDIDOSPENDIENTES";
    String ARMA_TU_PACK = "ARMATUPACK";
    String OFERTAS_ESPECIALES = "OFERTASESPECIALES";
    String MEDALLAS = "MEDALLAS";
    String ESIKAAHORA = "ESIKAAHORA";
}
