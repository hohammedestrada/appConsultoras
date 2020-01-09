package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    PremioEstadoType.BLOCKED,
    PremioEstadoType.ALLOWED,
    PremioEstadoType.CHOOSED
})
public @interface PremioEstadoType {
    int BLOCKED = 0;
    int ALLOWED = 1;
    int CHOOSED = 2;
}
