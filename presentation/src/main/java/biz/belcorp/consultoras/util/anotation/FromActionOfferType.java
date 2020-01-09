package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    FromActionOfferType.SHOW_MORE,
    FromActionOfferType.CATEGORY
})
public @interface FromActionOfferType {
    int SHOW_MORE = 1;
    int CATEGORY = 2;
}
