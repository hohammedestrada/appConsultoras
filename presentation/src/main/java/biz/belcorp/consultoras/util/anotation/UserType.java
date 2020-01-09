package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    UserType.CONSULTORA,
    UserType.POSTULANTE
})
public @interface UserType {
    int CONSULTORA = 1;
    int POSTULANTE = 2;
}
