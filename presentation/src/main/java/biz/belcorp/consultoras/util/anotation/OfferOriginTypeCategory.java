package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OfferOriginTypeCategory.ORIGIN_CAT_TODAS,
    OfferOriginTypeCategory.ORIGIN_CAT_FRAGANCIAS,
    OfferOriginTypeCategory.ORIGIN_CAT_MAQUILLAJE,
    OfferOriginTypeCategory.ORIGIN_CAT_CUIDADO,
    OfferOriginTypeCategory.ORIGIN_CAT_TRATAMIENTO,
    OfferOriginTypeCategory.ORIGIN_CAT_BIJOUTERIE,
    OfferOriginTypeCategory.ORIGIN_CAT_MODA
})
public @interface OfferOriginTypeCategory {
    String ORIGIN_CAT_TODAS = "100";
    String ORIGIN_CAT_FRAGANCIAS = "cat-fragancia";
    String ORIGIN_CAT_MAQUILLAJE = "cat-maquillaje";
    String ORIGIN_CAT_CUIDADO = "cat-cuidado-personal";
    String ORIGIN_CAT_TRATAMIENTO = "cat-tratamiento-facial";
    String ORIGIN_CAT_BIJOUTERIE = "cat-bijouterie";
    String ORIGIN_CAT_MODA = "cat-moda";
}
