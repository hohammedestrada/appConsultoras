package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    DigitalCatalogConfigCode.URL,
    DigitalCatalogConfigCode.ORDERS,
})
public @interface DigitalCatalogConfigCode {
    String URL = "UrlCatalogoDigital";              // Url de Catalogo Digital
    String ORDERS = "CantPedidosPendientes";        // Cantidad de pedidos pendientes
}
