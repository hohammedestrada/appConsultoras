package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    ObservationOrderCase.CONDITION,
    ObservationOrderCase.BACK_ORDER
})
public @interface ObservationOrderCase {
    int CONDITION = 7;
    int BACK_ORDER = 8;
}
