package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    CategoryFilter.MARCAS,
    CategoryFilter.PRECIOS
})
public @interface CategoryFilter {
    String MARCAS = "Marcas"; // Filtro por Marcas
    String PRECIOS = "Precios";   // Filtro por Precios

}
