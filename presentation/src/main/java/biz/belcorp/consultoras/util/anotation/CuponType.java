package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
    CuponType.REGISTRADO,
    CuponType.ACTIVADO,
    CuponType.USADO,
    CuponType.NONE
})
public @interface CuponType {
    int REGISTRADO = 1;
    int ACTIVADO = 2;
    int USADO = 3;
    int NONE = 0;
}
