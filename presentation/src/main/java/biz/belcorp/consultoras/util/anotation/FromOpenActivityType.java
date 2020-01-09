package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    FromOpenActivityType.ACTIVITY,
    FromOpenActivityType.NOTIFICATION
})
public @interface FromOpenActivityType {
    String ACTIVITY = "ACTIVITY";
    String NOTIFICATION = "NOTIFICATION";
}
