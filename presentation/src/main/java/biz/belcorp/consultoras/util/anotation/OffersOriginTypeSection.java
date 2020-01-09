package biz.belcorp.consultoras.util.anotation;


import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OffersOriginTypeSection.ORIGEN_BANNER,
    OffersOriginTypeSection.ORIGEN_ODD,
    OffersOriginTypeSection.ORIGEN_OPT,
    OffersOriginTypeSection.ORIGEN_SR,
    OffersOriginTypeSection.ORIGEN_LIQUIDACION,
    OffersOriginTypeSection.ORIGEN_ATP,
    OffersOriginTypeSection.ORIGEN_LMG,
    OffersOriginTypeSection.ORIGEN_LAN,
    OffersOriginTypeSection.ORIGEN_GND,
    OffersOriginTypeSection.ORIGEN_DUO_PERFECTO,
    OffersOriginTypeSection.ORIGEN_PACK_NUEVAS,
    OffersOriginTypeSection.ORIGEN_HV,
    OffersOriginTypeSection.ORIGEN_TODAS_OFERTAS,
    OffersOriginTypeSection.ORIGEN_FRAGANCIAS,
    OffersOriginTypeSection.ORIGEN_MAQUILLAJE,
    OffersOriginTypeSection.ORIGEN_CUIDADO_PERSONAL,
    OffersOriginTypeSection.ORIGEN_TRATAMIENTO_FACIAL,
    OffersOriginTypeSection.ORIGEN_BIJOUTERIE,
    OffersOriginTypeSection.ORIGEN_MODA,
    OffersOriginTypeSection.ORIGEN_DESPLEGABLE,
    OffersOriginTypeSection.ORIGEN_RESULTADOS,
    OffersOriginTypeSection.ORIGEN_DIGITADO,
    OffersOriginTypeSection.ORIGEN_RECOMENDADO,
    OffersOriginTypeSection.ORIGEN_OFERTA_FINAL,
    OffersOriginTypeSection.ORIGEN_SUGERIDO,
    OffersOriginTypeSection.ORIGEN_CROSSELLING,
    OffersOriginTypeSection.ORIGEN_UPSELLING,
    OffersOriginTypeSection.ORIGEN_PROMOCION_CONDICIONAL,
    OffersOriginTypeSection.ORIGEN_PROMOCION_PRODUCTO,
    OffersOriginTypeSection.ORIGEN_CAROUSEL_DEFAULT

})
public @interface OffersOriginTypeSection {
    String ORIGEN_BANNER = "001";
    String ORIGEN_ODD = "002";
    String ORIGEN_OPT = "003";
    String ORIGEN_SR = "004";
    String ORIGEN_LIQUIDACION = "005";
    String ORIGEN_ATP = "006";
    String ORIGEN_LMG = "007";
    String ORIGEN_LAN = "008";
    String ORIGEN_GND = "009";
    String ORIGEN_DUO_PERFECTO = "010";
    String ORIGEN_PACK_NUEVAS = "011";
    String ORIGEN_HV = "012";
    String ORIGEN_TODAS_OFERTAS = "013";
    String ORIGEN_FRAGANCIAS = "014";
    String ORIGEN_MAQUILLAJE = "015";
    String ORIGEN_CUIDADO_PERSONAL = "016";
    String ORIGEN_TRATAMIENTO_FACIAL = "017";
    String ORIGEN_BIJOUTERIE = "018";
    String ORIGEN_MODA = "019";
    String ORIGEN_DESPLEGABLE = "020";
    String ORIGEN_RESULTADOS = "021";
    String ORIGEN_DIGITADO = "022";
    String ORIGEN_RECOMENDADO = "023";
    String ORIGEN_OFERTA_FINAL= "024";
    String ORIGEN_SUGERIDO = "025";
    String ORIGEN_CROSSELLING = "026";
    String ORIGEN_UPSELLING = "027";
    String ORIGEN_PROMOCION_CONDICIONAL = "028";
    String ORIGEN_PROMOCION_PRODUCTO = "029";
    String ORIGEN_CAROUSEL_DEFAULT = "30";


}
