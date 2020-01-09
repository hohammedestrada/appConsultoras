package biz.belcorp.consultoras.util.anotation;


import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OffersOriginTypeSubSection.ORIGEN_FICHA,
    OffersOriginTypeSubSection.ORIGEN_CARRUSEL,
    OffersOriginTypeSubSection.ORIGEN_NULL

})
public @interface OffersOriginTypeSubSection {

    String ORIGEN_NULL = "001";
    String ORIGEN_FICHA = "002";
    String ORIGEN_CARRUSEL = "003";


}
