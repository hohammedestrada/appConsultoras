package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
    CardColor.MARCA,
    CardColor.CINCUENTA_ANIOS
})
public @interface CardColor {
    int MARCA = 1;
    int CINCUENTA_ANIOS = 2;
}
