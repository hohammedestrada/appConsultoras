package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    FichaFromAdd.ADD_DEFAULT,
    FichaFromAdd.ADD_CAROUSEL,
    FichaFromAdd.ADD_PROMOTION
})
public @interface FichaFromAdd {
    String ADD_DEFAULT = "AddDefault";
    String ADD_CAROUSEL = "AddCarousel";
    String ADD_PROMOTION = "AddPromotion";
}
