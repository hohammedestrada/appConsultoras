package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    AddedAlertType.DEFAULT,
    AddedAlertType.FESTIVAL
})
public @interface AddedAlertType {
    int DEFAULT = 1;
    int FESTIVAL = 2;
}
