package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    RevistaDigitalType.SIN_RD,
    RevistaDigitalType.CON_RD_SUSCRITA_ACTIVA,
    RevistaDigitalType.CON_RD_SUSCRITA_NO_ACTIVA,
    RevistaDigitalType.CON_RD_NO_SUSCRITA_ACTIVA,
    RevistaDigitalType.CON_RD_NO_SUSCRITA_NO_ACTIVA
})
public @interface RevistaDigitalType {
    int SIN_RD = 1;
    int CON_RD_SUSCRITA_ACTIVA = 2;
    int CON_RD_SUSCRITA_NO_ACTIVA = 3;
    int CON_RD_NO_SUSCRITA_ACTIVA = 4;
    int CON_RD_NO_SUSCRITA_NO_ACTIVA = 5;
}
