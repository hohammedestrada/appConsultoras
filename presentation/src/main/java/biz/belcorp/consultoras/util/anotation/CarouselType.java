package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    CarouselType.DEFAULT,
    CarouselType.UP_SELLING,
    CarouselType.CROSS_SELLING,
    CarouselType.SUGERIDOS,
    CarouselType.RECOMENDADOS,
    CarouselType.PROMO_PREMIO,
    CarouselType.PROMO_CONDICION
})
public @interface CarouselType {
    int DEFAULT = 0;
    int UP_SELLING = 1;
    int CROSS_SELLING = 2;
    int SUGERIDOS = 3;
    int RECOMENDADOS = 4;
    int PROMO_PREMIO = 5;
    int PROMO_CONDICION = 6;
}
