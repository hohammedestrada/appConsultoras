package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OffersOriginTypePalanca.ORIGIN_ATP,
    OffersOriginTypePalanca.ORIGIN_CAT,
    OffersOriginTypePalanca.ORIGIN_DIG,
    OffersOriginTypePalanca.ORIGIN_DP,
    OffersOriginTypePalanca.ORIGIN_GND,
    OffersOriginTypePalanca.ORIGIN_HV,
    OffersOriginTypePalanca.ORIGIN_LAN,
    OffersOriginTypePalanca.ORIGIN_LIQ,
    OffersOriginTypePalanca.ORIGIN_LMG,
    OffersOriginTypePalanca.ORIGIN_MG,
    OffersOriginTypePalanca.ORIGIN_ODD,
    OffersOriginTypePalanca.ORIGIN_OF,
    OffersOriginTypePalanca.ORIGIN_OPT,
    OffersOriginTypePalanca.ORIGIN_PN,
    OffersOriginTypePalanca.ORIGIN_SR
})

public @interface OffersOriginTypePalanca {
    String ORIGIN_ATP = "ATP"; // Arma Tu Pack
    String ORIGIN_CAT = "CAT"; // Catalogo
    String ORIGIN_DIG= "DIG"; // Digitado
    String ORIGIN_DP = "DP"; // Dúo Perfecto
    String ORIGIN_GND = "GND";   // GND
    String ORIGIN_HV = "HV";   // Herramientas de Venta
    String ORIGIN_LAN = "LAN"; // Lanzamientos
    String ORIGIN_LIQ = "LIQ"; // Liquidación
    String ORIGIN_LMG = "LMG"; // Ganadoras
    String ORIGIN_MG = "MG"; // Ganadoras
    String ORIGIN_ODD = "ODD"; // Oferta Del Día
    String ORIGIN_OF = "OF"; // Oferta Final
    String ORIGIN_OPT = "OPT"; // Ofertas Para Ti
    String ORIGIN_PN = "PN"; // Packs de Nuevas
    String ORIGIN_SR = "SR"; // Showroom

}
