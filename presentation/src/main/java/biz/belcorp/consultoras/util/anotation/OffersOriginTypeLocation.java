package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OffersOriginTypeLocation.ORIGEN_HOME,
    OffersOriginTypeLocation.ORIGEN_CONTENEDOR,
    OffersOriginTypeLocation.ORIGEN_LANDING,
    OffersOriginTypeLocation.ORIGEN_BUSCADOR,
    OffersOriginTypeLocation.ORIGEN_PEDIDO,
    OffersOriginTypeLocation.ORIGEN_FICHA
})
public @interface OffersOriginTypeLocation {
    String ORIGEN_HOME = "001";
    String ORIGEN_CONTENEDOR = "002";
    String ORIGEN_LANDING = "003";
    String ORIGEN_BUSCADOR = "004";
    String ORIGEN_PEDIDO = "005";
    String ORIGEN_FICHA = "006";
}
