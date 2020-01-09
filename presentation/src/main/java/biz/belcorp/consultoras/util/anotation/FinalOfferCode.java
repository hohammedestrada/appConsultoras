package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    FinalOfferCode.OK,
    FinalOfferCode.ERROR_PRODUCTO_NOEXISTE,
    FinalOfferCode.ERROR_PRODUCTO_AGOTADO,
    FinalOfferCode.ERROR_PRODUCTO_LIQUIDACION,
    FinalOfferCode.ERROR_PRODUCTO_OFERTAREVISTA_ESIKA,
    FinalOfferCode.ERROR_PRODUCTO_OFERTAREVISTA_LBEL,
    FinalOfferCode.ERROR_PRODUCTO_ESTRATEGIA
})
public @interface FinalOfferCode {

    String OK = "0000";
    String ERROR_PRODUCTO_NOEXISTE = "1101";
    String ERROR_PRODUCTO_AGOTADO = "1102";
    String ERROR_PRODUCTO_LIQUIDACION = "1103";
    String ERROR_PRODUCTO_OFERTAREVISTA_ESIKA = "1106";
    String ERROR_PRODUCTO_OFERTAREVISTA_LBEL = "1107";
    String ERROR_PRODUCTO_ESTRATEGIA = "1108";

}
