package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    MagazineSuscriptionType.DEFAULT,
    MagazineSuscriptionType.WITHOUT_GND,
    MagazineSuscriptionType.WITH_GND_SA,
    MagazineSuscriptionType.WITH_GND_SNA,
    MagazineSuscriptionType.WITH_GND_NSA,
    MagazineSuscriptionType.WITH_GND_NSNA
})
public @interface MagazineSuscriptionType {
    int DEFAULT = 0;        // Default
    int WITHOUT_GND = 1;    // País sin guía de negocio digital
    int WITH_GND_SA = 2;    // País con guía de negocio digital consultora suscrita activa
    int WITH_GND_SNA = 3;   // País con guía de negocio digital consultora suscrita no activa
    int WITH_GND_NSA = 4;   // País con guía de negocio digital consultora no suscrita activa
    int WITH_GND_NSNA = 5;  // País con guía de negocio digital consultora no suscrita no activa
}
