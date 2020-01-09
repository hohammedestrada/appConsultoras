package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    NetworkEventType.CONNECTION_AVAILABLE,
    NetworkEventType.CONNECTION_NOT_AVAILABLE,
    NetworkEventType.DATAMI_AVAILABLE,
    NetworkEventType.DATAMI_NOT_AVAILABLE,
    NetworkEventType.WIFI
})
public @interface NetworkEventType {
    int CONNECTION_NOT_AVAILABLE = 0;
    int CONNECTION_AVAILABLE = 1;
    int DATAMI_NOT_AVAILABLE = 2;
    int DATAMI_AVAILABLE = 3;
    int WIFI = 4;
    int MULTI_ORDER_AVAILABLE = 5;
}
