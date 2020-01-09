package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    FestPremioStateType.INSERT_UPDATE,
    FestPremioStateType.DELETE
})
public @interface FestPremioStateType {
    int INSERT_UPDATE = 1;
    int DELETE = 0;
}
