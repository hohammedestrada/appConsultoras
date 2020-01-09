package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    EnterateMasType.MIACADEMIA,
    EnterateMasType.ISSUU
})
public @interface EnterateMasType {
    int MIACADEMIA = 1;
    int ISSUU = 2;
}
