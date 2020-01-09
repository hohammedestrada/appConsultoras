package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    BrandType.LBEL,
    BrandType.ESIKA,
    BrandType.CYZONE,
    BrandType.FINART
})
public @interface BrandType {

    String LBEL = "L'Bel";      // 1
    String ESIKA  = "Esika";    // 2
    String CYZONE = "Cyzone";   // 3
    String FINART = "Finart";   // 4
    String LBEL_ORIGEN = "Lbel"; // 5

}
