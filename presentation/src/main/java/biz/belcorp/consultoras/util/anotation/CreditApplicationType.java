package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    CreditApplicationType.NOT_APPLY,
    CreditApplicationType.NOT_ACCEPT,
    CreditApplicationType.ACCEPT
})
public @interface CreditApplicationType {
    int NOT_APPLY = -1;
    int NOT_ACCEPT = 0;
    int ACCEPT = 1;
}
