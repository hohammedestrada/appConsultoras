package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    FromActionSearchProduct.CATEGORY,
    FromActionSearchProduct.FILTER
})
public @interface FromActionSearchProduct {
    int CATEGORY = 1;
    int FILTER = 2;
}
