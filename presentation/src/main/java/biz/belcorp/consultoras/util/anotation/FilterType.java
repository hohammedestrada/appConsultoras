package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    FilterType.CATEGORY,
    FilterType.BRAND,
    FilterType.PRICE
})
public @interface FilterType {
    String CATEGORY = "Categorías";
    String BRAND = "Marcas";
    String PRICE = "Precios";
}
