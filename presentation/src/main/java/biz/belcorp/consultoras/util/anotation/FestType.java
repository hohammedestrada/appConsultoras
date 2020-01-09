package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    FestType.FEST_CONDICION,
    FestType.FEST_PREMIO
})
public @interface FestType {
    int FEST_CONDICION = 1;
    int FEST_PREMIO = 2;
}
