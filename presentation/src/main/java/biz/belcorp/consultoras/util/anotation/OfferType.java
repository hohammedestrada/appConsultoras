package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OfferType.KIT_CAMINO_BRILLANTE,
    OfferType.DEMOSTRADOR_CAMINO_BRILLANTE
})
public @interface OfferType {
    String KIT_CAMINO_BRILLANTE = "CBK"; //Camino brillante kit
    String DEMOSTRADOR_CAMINO_BRILLANTE = "CBD"; //Camino brillante demostrador
}
