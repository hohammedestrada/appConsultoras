package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OrderOriginCode.DEFAULT,
    OrderOriginCode.RELATED_OFFER,
    OrderOriginCode.CAMINO_BRILLANTE_OFERTAS_OFERTAS_ESPECIALES_CARRUSEL,
    OrderOriginCode.CAMINO_BRILLANTE_HOME_OFERTAS_ESPECIALES_FICHA,
    OrderOriginCode.CAMINO_BRILLANTE_HOME_OFERTAS_ESPECIALES_CARRUSEL,
    OrderOriginCode.CAMINO_BRILLANTE_OFERTAS_OFERTAS_ESPECIALES_FICHA
})
public @interface OrderOriginCode {
    String DEFAULT = "4";
    String RELATED_OFFER = "4021501";

    String CAMINO_BRILLANTE_OFERTAS_OFERTAS_ESPECIALES_CARRUSEL = "4181901";
    String CAMINO_BRILLANTE_HOME_OFERTAS_ESPECIALES_FICHA = "4181902";
    String CAMINO_BRILLANTE_HOME_OFERTAS_ESPECIALES_CARRUSEL = "4201901";
    String CAMINO_BRILLANTE_OFERTAS_OFERTAS_ESPECIALES_FICHA = "4201902";

}
