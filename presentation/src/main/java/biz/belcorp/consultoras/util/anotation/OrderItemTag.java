package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    OrderItemTag.TAG_FEST
})
public @interface OrderItemTag {
    int TAG_FEST = 0;
}
